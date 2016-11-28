package rss_dashboard.server.network;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.server.model.dashboard.Dashboard;
import rss_dashboard.server.model.dashboard.DashboardElement;
import rss_dashboard.server.model.misc.AuthorizationException;
import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.rss.RssChannel;
import rss_dashboard.server.repository.DashboardElementRepository;
import rss_dashboard.server.repository.RepositoryException;
import rss_dashboard.server.repository.RssChannelRepository;

@Path("/dashboard")
public class DashboardHttpServlet extends AbstractHttpServlet {
	@GET
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboard getDashboard() {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}
		
		response.setCharacterEncoding("UTF-8");

		// fetch dashboard element from the database
		List<DashboardElement> dashboardElements;

		try {
			DashboardElementRepository dashboardElementRepository = new DashboardElementRepository();

			dashboardElements = dashboardElementRepository
					.query(DashboardElement.builder().clientId(profile.getId()).build());
		} catch (RepositoryException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}

		// process elements, transform them to Dashboard object
		IDashboard dashboard = Dashboard.builder().layout(new ArrayList<>()).build();

		int i = 0;

		for (int p = 0; p < 10; p++) {
			dashboard.getLayout().add(new String[3][2]);

			String[][] page = dashboard.getLayout().get(p);

			for (int e = i; e < dashboardElements.size(); e++) {
				if (dashboardElements.get(e).getPage() > p) {
					break;
				}

				DashboardElement dashboardElement = dashboardElements.get(e);

				page[dashboardElement.getRow()][dashboardElement.getColumn()] = dashboardElement.getChannelId();

				i++;
			}
		}

		return dashboard;
	}

	@DELETE
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteDashboard(@QueryParam("pageId") int pageId, @QueryParam("rowId") int rowId,
			@QueryParam("columnId") int columnId) {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return;
		}

		// fetch dashboard element from the database
		try {
			DashboardElementRepository dashboardElementRepository = new DashboardElementRepository();

			List<DashboardElement> dashboardElements = dashboardElementRepository
					.query(DashboardElement.builder().clientId(profile.getId()).build());

			for (DashboardElement dashboardElement : dashboardElements) {
				if (dashboardElement.getPage() == pageId && dashboardElement.getRow() == rowId
						&& dashboardElement.getColumn() == columnId) {
					dashboardElementRepository.remove(dashboardElement.getId());

					break;
				}
			}
		} catch (RepositoryException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			e.printStackTrace();
		}
	}

	@POST
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void postDashboard(@QueryParam("pageId") int pageId, @QueryParam("rowId") int rowId,
			@QueryParam("columnId") int columnId, @QueryParam("feedUrl") String feedUrl) {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return;
		}

		// get channel's details with ROME
		try {
			RssChannel rssChannel = RssChannel.builder().link(feedUrl).build();

			if (rssChannel.loadDetails()) {
				RssChannelRepository rssChannelRepository = new RssChannelRepository();
				
				String channelId = rssChannelRepository.add(rssChannel);

				DashboardElement dashboardElement = DashboardElement.builder().row(rowId).column(columnId).page(pageId)
						.clientId(profile.getId()).channelId(channelId).build();

				DashboardElementRepository dashboardElementRepository = new DashboardElementRepository();

				dashboardElementRepository.add(dashboardElement);
			} else {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			}
		} catch (RepositoryException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			e.printStackTrace();
		}
	}
}
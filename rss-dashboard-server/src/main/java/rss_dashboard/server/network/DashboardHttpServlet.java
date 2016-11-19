package rss_dashboard.server.network;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.common.model.dashboard.IDashboardMapping;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.server.model.dashboard.DashboardMapping;
import rss_dashboard.server.model.dashboard.DashboardLayout;
import rss_dashboard.server.model.misc.ClientProfile;

@Path("/dashboard")
public class DashboardHttpServlet extends AbstractHttpServlet {
	@GET
	@Path("/mapping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardMapping getDashboardMapping(
			@QueryParam("dateFrom") long dateFrom,
			@QueryParam("dateTill") long dateTill,
			@QueryParam("categories") String categoriesStr) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return DashboardMapping.builder().build();
	}

	@GET
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardLayout getDashboardLayout() {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return DashboardLayout.builder().build();
	}

	@POST
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteDashboardLayout(
			@QueryParam("pageId") String pageId,
			@QueryParam("rowId") String rowId,
			@QueryParam("columnId") String columnId,
			@QueryParam("feedUrl") String feedUrl) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return;
		}

		// TODO
	}

	@DELETE
	@Path("/layout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void postDashboardLayout(
			@QueryParam("pageId") int pageId,
			@QueryParam("rowId") int rowId,
			@QueryParam("columnId") int columnId,
			@QueryParam("feedUrl") String feedUrl) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return;
		}

		// TODO
	}
}

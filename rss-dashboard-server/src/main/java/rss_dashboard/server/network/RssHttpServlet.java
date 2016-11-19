package rss_dashboard.server.network;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.rss.RssChannel;
import rss_dashboard.server.model.rss.RssItem;

@Path("/rss")
public class RssHttpServlet extends AbstractHttpServlet {
	@GET
	@Path("/channels")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssChannel getRssChannel(
			@QueryParam("id") String id) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return RssChannel.builder().build();
	}

	@GET
	@Path("/items")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssItem getRssItem(
			@QueryParam("id") String id) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return RssItem.builder().build();
	}
}

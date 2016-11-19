package rss_dashboard.server.network;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.rss.RssChannel;
import rss_dashboard.server.model.rss.RssItem;

@Path("/rss")
public class RssServlet extends AbstractServlet {
	@GET
	@Path("/channels/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssChannel getRssChannel(
			@PathParam("id") String id) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return RssChannel.builder().build();
	}

	@GET
	@Path("/items/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssItem getRssItem(
			@PathParam("id") String id) {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		// TODO
		return RssItem.builder().build();
	}
}

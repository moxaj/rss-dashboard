package rss_dashboard.server.network;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.server.model.misc.ClientProfile;

@Path("/misc")
public class MiscHttpServlet extends AbstractHttpServlet {
	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String login() throws IOException {
		String authorization = getBasicAuthorization();
		if (authorization == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return "";
		}

		String[] authorizationParts = authorization.split(";");
		if (authorizationParts.length != 2) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return "";
		}

		String username = authorizationParts[0];
		String password = authorizationParts[1];

		// TODO
		return "dummy_token";
	}

	@POST
	@Path("/logout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void logout() {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return;
		}

		// TODO
	}

	@GET
	@Path("/keepalive")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public boolean doKeepAlive() throws IOException {
		ClientProfile profile = getClientProfile();
		if (profile == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return false;
		}

		// TODO
		return true;
	}
}

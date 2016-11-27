package rss_dashboard.server.network;

import java.io.IOException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.util.HttpStatus;

import rss_dashboard.server.model.misc.AuthorizationException;
import rss_dashboard.server.model.misc.AuthorizationProviders;
import rss_dashboard.server.model.misc.IAuthorizationHelper;

@Path("/misc")
public class MiscHttpServlet extends AbstractHttpServlet {
	@POST
	@Path("/login")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String login() throws IOException {
		String authorization = getBasicAuthorization();

		// validate authorization field
		String[] authorizationParts;

		try {
			authorizationParts = splitAuthorization(authorization);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return "";
		}

		// provider-dependent helper instantiation
		IAuthorizationHelper authorizationHelper;
		
		try {
			authorizationHelper = createAuthorizationHelper(AuthorizationProviders.fromText(authorizationParts[0]));
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return "";
		}

		// do authorization and get token
		String token = null;

		try {
			token = authorizationHelper.authorize(authorizationParts[1]);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return "";
		}

		return token;
	}

	@POST
	@Path("/logout")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void logout() {
		String authorization = getBasicAuthorization();

		// validate authorization field
		String[] authorizationParts;

		try {
			authorizationParts = splitAuthorization(authorization);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return;
		}

		// provider-dependent helper instantiation
		IAuthorizationHelper authorizationHelper;
		
		try {
			authorizationHelper = createAuthorizationHelper(AuthorizationProviders.fromText(authorizationParts[0]));
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return;
		}

		// do deauthorization
		try {
			authorizationHelper.deauthorize(authorizationParts[1]);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return;
		}
	}
}

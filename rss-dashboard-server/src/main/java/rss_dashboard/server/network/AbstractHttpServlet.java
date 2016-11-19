package rss_dashboard.server.network;

import javax.ws.rs.core.Context;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import rss_dashboard.server.model.misc.ClientProfile;

public abstract class AbstractHttpServlet {
	@Context
	protected Request request;
	@Context
	protected Response response;

	protected final String getBasicAuthorization() {
		String authorization = request.getHeader("Authorization");
		return authorization == null || !authorization.startsWith("Basic ")
				? null
				: authorization.substring("Basic ".length());
	}

	protected final ClientProfile getClientProfile() {
		String token = getBasicAuthorization();
		if (token == null) {
			return null;
		}

		// TODO
		return new ClientProfile();
	}
}

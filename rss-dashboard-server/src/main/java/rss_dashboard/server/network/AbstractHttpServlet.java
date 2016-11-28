package rss_dashboard.server.network;

import java.util.List;

import javax.ws.rs.core.Context;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import rss_dashboard.server.model.misc.AuthorizationException;
import rss_dashboard.server.model.misc.AuthorizationProviders;
import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.misc.GoogleAuthorizationHelper;
import rss_dashboard.server.model.misc.IAuthorizationHelper;
import rss_dashboard.server.repository.ClientProfileRepository;
import rss_dashboard.server.repository.RepositoryException;

public abstract class AbstractHttpServlet {
	@Context
	protected Request request;
	@Context
	protected Response response;

	protected final String getBasicAuthorization() {
		String authorization = request.getHeader("Authorization");

		return authorization == null || !authorization.startsWith("Basic ") ? null
				: authorization.substring("Basic ".length());
	}

	protected final ClientProfile getClientProfile() throws AuthorizationException {
		/*String authorization = getBasicAuthorization();

		// validate authorization field
		String[] authorizationParts;

		try {
			authorizationParts = splitAuthorization(authorization);
		} catch (AuthorizationException e) {
			e.printStackTrace();
			return null;
		}

		// fetching client data
		ClientProfile clientProfile = null;
		
		try {
			ClientProfileRepository clientProfileRepository = new ClientProfileRepository();

			List<ClientProfile> clientProfiles = clientProfileRepository
					.query(ClientProfile.builder().token1(authorizationParts[1]).build());
			
			if (clientProfiles.size() == 0) {
				return null;
			}
			
			clientProfile = clientProfiles.get(0);
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during client data fetching.");
		}
		
		// reauthorize if valid and expired
		if (clientProfile.isExpired()) {
			// provider-dependent helper instantiation
			IAuthorizationHelper authorizationHelper;
			
			try {
				authorizationHelper = createAuthorizationHelper(AuthorizationProviders.fromText(authorizationParts[0]));
				
				authorizationHelper.reauthorize(clientProfile);
			} catch (AuthorizationException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return clientProfile;*/
		
		return ClientProfile.builder().id("DUMMYID").email("dummy@dummy.com").build();
	}

	protected String[] splitAuthorization(String authorization) throws AuthorizationException {
		// must be filled
		if (authorization == null) {
			throw new AuthorizationException("Authorization header is empty.");
		}

		// format: provider;authorization_data
		String[] authorizationParts = authorization.split(";", 2);

		if (authorizationParts.length != 2) {
			throw new AuthorizationException("Authorization header is invalid.");
		}

		return authorizationParts;
	}

	protected IAuthorizationHelper createAuthorizationHelper(AuthorizationProviders provider)
			throws AuthorizationException {
		switch (provider) {
		case GOOGLE:
			return new GoogleAuthorizationHelper();
		default:
			throw new AuthorizationException("Authorization provider is invalid.");
		}
	}
}

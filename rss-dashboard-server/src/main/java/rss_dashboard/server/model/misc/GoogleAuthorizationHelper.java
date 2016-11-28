package rss_dashboard.server.model.misc;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import rss_dashboard.server.repository.ClientProfileRepository;
import rss_dashboard.server.repository.RepositoryException;

public class GoogleAuthorizationHelper implements IAuthorizationHelper {
	@Override
	public void authorize(String data1, String data2) throws AuthorizationException {
		// expected: data1 = authCode, data2 = client token

		// load secrets
		GoogleClientSecrets clientSecrets = loadSecrets();

		// exchange the authorization code for an access token
		GoogleTokenResponse tokenResponse = null;

		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					JacksonFactory.getDefaultInstance(), "https://www.googleapis.com/oauth2/v4/token",
					clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), data1, null)
							.execute();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during token exchange.");
		}

		String aToken = tokenResponse.getAccessToken();
		String rToken = tokenResponse.getRefreshToken();
		Long expiration = tokenResponse.getExpiresInSeconds();

		// store tokens and bind these to a user (email)
		GoogleIdToken idToken = null;

		try {
			idToken = tokenResponse.parseIdToken();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during id token parsing.");
		}

		GoogleIdToken.Payload payload = idToken.getPayload();

		try {
			ClientProfileRepository clientProfileRepository = new ClientProfileRepository();

			List<ClientProfile> clientProfiles = clientProfileRepository
					.query(ClientProfile.builder().email(payload.getEmail()).build());

			if (clientProfiles.size() != 0) {
				ClientProfile clientProfile = clientProfiles.get(0);

				clientProfile.setToken1(aToken);
				clientProfile.setExpiration(LocalDateTime.now().plusSeconds(expiration));
				clientProfile.setToken2(rToken);
				clientProfile.setToken3(data2);
				clientProfile.setProvider(AuthorizationProviders.GOOGLE);

				clientProfileRepository.update(clientProfile);
			} else {
				clientProfileRepository.add(ClientProfile.builder().email(payload.getEmail()).token1(aToken)
						.expiration(LocalDateTime.now().plusSeconds(expiration)).token2(rToken).token3(data2)
						.provider(AuthorizationProviders.GOOGLE).build());
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during server client persisting.");
		}
	}

	@Override
	public void deauthorize(String data) throws AuthorizationException {
		// expected: data = client token

		// delete access token from client in database
		ClientProfile clientProfile = null;

		try {
			ClientProfileRepository clientProfileRepository = new ClientProfileRepository();

			List<ClientProfile> clientProfiles = clientProfileRepository
					.query(ClientProfile.builder().token3(data).build());

			if (clientProfiles.size() != 0) {
				clientProfile = clientProfiles.get(0);

				clientProfile.setToken1(null);
				clientProfile.setToken2(null);
				clientProfile.setToken3(null);
				clientProfile.setExpiration(LocalDateTime.now());

				clientProfileRepository.update(clientProfile);
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during server client persisting.");
		}

		// send HTTP GET request to Google
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet httpGet = new HttpGet("https://accounts.google.com/o/oauth2/revoke?token=" + clientProfile.getToken1());

		CloseableHttpResponse httpResponse = null;

		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during token revoking.");
		} finally {
			try {
				httpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (httpResponse.getStatusLine().getStatusCode() != 200) {
			throw new AuthorizationException("Not good (not 200) response code for token revoking.");
		}
	}

	@Override
	public void reauthorize(ClientProfile clientProfile) throws AuthorizationException {
		if (!clientProfile.isValid() || clientProfile.getToken2() == null || clientProfile.getToken2().isEmpty()) {
			throw new AuthorizationException("Client is invalid or refresh token is missing. Cannot be reauthorized.");
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

		// load secrets
		GoogleClientSecrets clientSecrets = loadSecrets();

		// build request payload
		List<NameValuePair> parameters = new ArrayList<>();
		parameters.add(new BasicNameValuePair("client_id", clientSecrets.getDetails().getClientId()));
		parameters.add(new BasicNameValuePair("client_secret", clientSecrets.getDetails().getClientSecret()));
		parameters.add(new BasicNameValuePair("refresh_token", clientProfile.getToken2()));
		parameters.add(new BasicNameValuePair("grant_type", "refresh_token"));

		CloseableHttpResponse httpResponse = null;
		JSONObject jsonResponse = null;

		// send request and get response
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));

			httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new AuthorizationException("Not good (not 200) response code for token refreshing.");
			}

			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpEntity == null) {
				throw new AuthorizationException("Token refreshing has no response content.");
			}

			jsonResponse = new JSONObject(EntityUtils.toString(httpEntity));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during token refreshing.");
		} finally {
			try {
				httpResponse.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// process response
		String aToken = jsonResponse.getString("access_token");
		Long expiration = jsonResponse.getLong("expires_in");

		clientProfile.setToken1(aToken);
		clientProfile.setExpiration(LocalDateTime.now().plusSeconds(expiration));

		try {
			ClientProfileRepository clientProfileRepository = new ClientProfileRepository();

			clientProfileRepository.update(clientProfile);
		} catch (RepositoryException e) {
			e.printStackTrace();
			throw new AuthorizationException("Error during server client persisting.");
		}
	}

	private GoogleClientSecrets loadSecrets() throws AuthorizationException {
		GoogleClientSecrets clientSecrets = null;

		try {
			clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
					new FileReader("src/main/resources/google_secret.json"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("Google secrets file does not exist.");
		}

		return clientSecrets;
	}
}

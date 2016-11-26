package rss_dashboard.server.model.misc;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleAuthorizationHelper implements IAuthorizationHelper {
	@Override
	public String authorize(String data) throws AuthorizationException {
		// expected: data = authCode

		// load secrets
		GoogleClientSecrets clientSecrets = null;

		try {
			clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(),
					new FileReader("src/main/resources/google_secret.json"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("secret_file_does_not_exist");
		}

		// exchange the authorization code for an access token
		GoogleTokenResponse tokenResponse = null;

		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					JacksonFactory.getDefaultInstance(), "https://www.googleapis.com/oauth2/v4/token",
					clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret(), data, "")
							.execute();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("token_exchange_remote_error");
		}

		String aToken = tokenResponse.getAccessToken();
		String rToken = tokenResponse.getRefreshToken();

		// store tokens and bind these to a user
		// TODO

		return aToken;
	}

	@Override
	public void deauthorize(String data) throws AuthorizationException {
		// expected: data = access token

		try {
			URL url = new URL("https://www.googleapis.com/oauth2/v4/revoke?token=" + data);

			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			if (connection.getResponseCode() != 200) {
				connection.disconnect();
				throw new AuthorizationException("not_good_response_code");
			}

			// delete access token from database
			// TODO

			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AuthorizationException("remote_error");
		}
	}
}

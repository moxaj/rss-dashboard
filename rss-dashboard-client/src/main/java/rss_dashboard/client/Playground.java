package rss_dashboard.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ObjectParser;

import rss_dashboard.client.network.misc.LoginRequest;
import rss_dashboard.client.network.misc.LoginResponse;
import rss_dashboard.common.network.misc.ILoginRequest;
import rss_dashboard.common.network.misc.ILoginResponse;

public class Playground {
	public static void main(String[] args) throws MalformedURLException, IOException {
		JsonFactory jsonFactory = new GsonFactory();
		ObjectParser parser = new JsonObjectParser(jsonFactory);
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory httpFactory = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				request.setParser(parser);
			}
		});

		ILoginRequest loginRequest = LoginRequest.builder().username("foo").password("bar").build();
		HttpRequest httpRequest = httpFactory.buildRequest(
				"GET",
				new GenericUrl(new URL("https://localhost:8080/login")),
				new JsonHttpContent(jsonFactory, loginRequest));

		ILoginResponse loginResponse = httpRequest.execute().parseAs(LoginResponse.class);
		System.out.println(String.format("Token received: '%s'.", loginResponse.getToken()));
	}
}

package rss_dashboard.client.network;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ObjectParser;

import rss_dashboard.client.model.dashboard.Dashboard;
import rss_dashboard.client.model.rss.RssChannel;
import rss_dashboard.client.model.rss.RssChannelMapping;
import rss_dashboard.client.model.rss.RssItem;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public class HttpClient implements INetworkClient {
	private static final JsonFactory JSON_FACTORY;
	private static final HttpRequestFactory HTTP_REQUEST_FACTORY;
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(64);
	static {
		JSON_FACTORY = new GsonFactory();
		ObjectParser objectParser = new JsonObjectParser(JSON_FACTORY);
		HttpTransport httpTransport = new NetHttpTransport();
		HTTP_REQUEST_FACTORY = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(objectParser);
				request.getHeaders().setAccept("application/json");
				request.getHeaders().setContentType("application/json; charset=utf-8");
				request.getHeaders().setCacheControl("no-store");
			}
		});
	}

	private final URL miscLoginUrl;
	private final URL miscLogoutUrl;
	private final URL rssChannelUrl;
	private final URL rssItemUrl;
	private final URL rssChannelMappingUrl;
	private final URL dashboardLayoutUrl;

	public HttpClient(String baseUrlString) throws MalformedURLException {
		miscLoginUrl = new URL(baseUrlString + "/misc/login");
		miscLogoutUrl = new URL(baseUrlString + "/misc/logout");
		rssChannelUrl = new URL(baseUrlString + "/rss/channels");
		rssItemUrl = new URL(baseUrlString + "/rss/items");
		rssChannelMappingUrl = new URL(baseUrlString + "/rss/mapping");
		dashboardLayoutUrl = new URL(baseUrlString + "/dashboard/layout");
	}

	@Override
	public CompletableFuture<String> login(String username, String password) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(new GenericUrl(miscLoginUrl),
						new EmptyContent());
				request.getHeaders().setAuthorization(String.format("Basic %s;%s", username, password));
				return request.execute().parseAsString();
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<Void> logout(String token) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(new GenericUrl(miscLogoutUrl),
						new EmptyContent());
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				request.execute();
				return null;
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IRssChannel> getRssChannel(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				GenericUrl url = new GenericUrl(rssChannelUrl);
				url.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(url);
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				return request.execute().parseAs(RssChannel.class);
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IRssItem> getRssItem(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				GenericUrl url = new GenericUrl(rssItemUrl);
				url.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(url);
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				return request.execute().parseAs(RssItem.class);
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IRssChannelMapping> getRssChannelMapping(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				GenericUrl url = new GenericUrl(rssChannelMappingUrl);
				url.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(url);
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				return request.execute().parseAs(RssChannelMapping.class);
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IDashboard> getDashboard(String token) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(new GenericUrl(dashboardLayoutUrl));
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				return request.execute().parseAs(Dashboard.class);
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<Void> modifyDashboardLayout(String token, int pageId, int rowId, int columnId,
			String feedUrl) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				GenericUrl url = new GenericUrl(dashboardLayoutUrl);
				url.set("pageId", pageId);
				url.set("rowId", rowId);
				url.set("columnId", columnId);
				url.set("feedUrl", feedUrl);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildRequest(
						feedUrl == null ? "DELETE" : "POST",
						url,
						new EmptyContent());
				request.getHeaders().setAuthorization(String.format("Basic %s", token));
				request.execute();
				return null;
			} catch (Throwable e) {
				throw new CompletionException(e);
			}
		}, EXECUTOR);
	}

	@Override
	public void shutdown() {
		EXECUTOR.shutdown();
	}
}

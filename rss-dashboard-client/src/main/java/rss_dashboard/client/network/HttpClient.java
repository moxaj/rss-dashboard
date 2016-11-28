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
				request.getHeaders().set("keepalive", true);
			}
		});
	}

	private final GenericUrl miscLoginUrl;
	private final GenericUrl miscLogoutUrl;
	private final GenericUrl rssChannelUrl;
	private final GenericUrl rssItemUrl;
	private final GenericUrl rssChannelMappingUrl;
	private final GenericUrl dashboardLayoutUrl;

	public HttpClient(String baseUrlString) throws MalformedURLException {
		miscLoginUrl = new GenericUrl(new URL(baseUrlString + "/misc/login"));
		miscLogoutUrl = new GenericUrl(new URL(baseUrlString + "/misc/logout"));
		rssChannelUrl = new GenericUrl(new URL(baseUrlString + "/rss/channels"));
		rssItemUrl = new GenericUrl(new URL(baseUrlString + "/rss/items"));
		rssChannelMappingUrl = new GenericUrl(new URL(baseUrlString + "/rss/mapping"));
		dashboardLayoutUrl = new GenericUrl(new URL(baseUrlString + "/dashboard/layout"));
	}

	@Override
	public CompletableFuture<String> login(String username, String password) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(miscLoginUrl, new EmptyContent());
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
				HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(miscLogoutUrl, new EmptyContent());
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
				rssChannelUrl.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(rssChannelUrl);
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
				rssItemUrl.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(rssItemUrl);
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
				rssChannelMappingUrl.set("id", id);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(rssChannelMappingUrl);
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
				HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(dashboardLayoutUrl);
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
				dashboardLayoutUrl.set("pageId", pageId);
				dashboardLayoutUrl.set("rowId", rowId);
				dashboardLayoutUrl.set("columnId", columnId);
				dashboardLayoutUrl.set("feedUrl", feedUrl);
				HttpRequest request = HTTP_REQUEST_FACTORY.buildRequest(
						feedUrl == null ? "DELETE" : "POST",
						dashboardLayoutUrl,
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

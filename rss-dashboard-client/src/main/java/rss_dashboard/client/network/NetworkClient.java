package rss_dashboard.client.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
import rss_dashboard.client.model.dashboard.DashboardLayout;
import rss_dashboard.client.model.rss.RssChannel;
import rss_dashboard.client.model.rss.RssItem;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;

public class NetworkClient implements INetworkClient {
	private static final JsonFactory JSON_FACTORY;
	private static final HttpRequestFactory HTTP_REQUEST_FACTORY;
	static {
		JSON_FACTORY = new GsonFactory();
		ObjectParser objectParser = new JsonObjectParser(JSON_FACTORY);
		HttpTransport httpTransport = new NetHttpTransport();
		HTTP_REQUEST_FACTORY = httpTransport.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
				request.setParser(objectParser);
				request.getHeaders().setAccept("application/json");
				request.getHeaders().setContentType("application/json");
				request.getHeaders().set("keepalive", true);
			}
		});
	}

	private final GenericUrl loginUrl;
	private final GenericUrl logoutUrl;
	private final GenericUrl keepAliveUrl;
	private final GenericUrl rssChannelUrl;
	private final GenericUrl rssItemUrl;
	private final GenericUrl dashboardUrl;
	private final GenericUrl dashboardLayoutUrl;

	public NetworkClient(String baseUrlString) throws MalformedURLException {
		loginUrl = new GenericUrl(new URL(baseUrlString + "/misc/login"));
		logoutUrl = new GenericUrl(new URL(baseUrlString + "/misc/logout"));
		keepAliveUrl = new GenericUrl(new URL(baseUrlString + "/misc/keepAlive"));
		rssChannelUrl = new GenericUrl(new URL(baseUrlString + "/rss/channels"));
		rssItemUrl = new GenericUrl(new URL(baseUrlString + "/rss/items"));
		dashboardUrl = new GenericUrl(new URL(baseUrlString + "/dashboard/dashboard"));
		dashboardLayoutUrl = new GenericUrl(new URL(baseUrlString + "/dashboard/layout"));
	}

	@Override
	public String login(String username, String password) throws IOException {
		HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(loginUrl, new EmptyContent());
		request.getHeaders().setAuthorization(String.format("Basic %s;%s", username, password));
		return request.execute().parseAsString();
	}

	@Override
	public void logout(String token) throws IOException {
		HttpRequest request = HTTP_REQUEST_FACTORY.buildPostRequest(logoutUrl, new EmptyContent());
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		request.execute();
	}

	@Override
	public boolean doKeepAlive(String token) throws IOException {
		HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(keepAliveUrl);
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		return "true".equals(request.execute().parseAsString());
	}

	@Override
	public IRssChannel getRssChannel(String token, String id) throws IOException {
		rssChannelUrl.set("id", id);
		HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(rssChannelUrl);
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		return request.execute().parseAs(RssChannel.class);
	}

	@Override
	public IRssItem getRssItem(String token, String id) throws IOException {
		rssItemUrl.set("id", id);
		HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(rssItemUrl);
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		return request.execute().parseAs(RssItem.class);
	}

	@Override
	public IDashboard getDashboard(String token, long dateFrom, long dateTill, List<String> categories)
			throws IOException {
		dashboardUrl.set("dateFrom", dateFrom);
		dashboardUrl.set("dateTill", dateTill);
		dashboardUrl.set("categories", String.join("/", categories));
		HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(dashboardUrl);
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		return request.execute().parseAs(Dashboard.class);
	}

	@Override
	public IDashboardLayout getDashboardLayout(String token) throws IOException {
		HttpRequest request = HTTP_REQUEST_FACTORY.buildGetRequest(dashboardLayoutUrl);
		request.getHeaders().setAuthorization(String.format("Basic %s", token));
		return request.execute().parseAs(DashboardLayout.class);
	}

	@Override
	public void modifyDashboardLayout(String token, int pageId, int rowId, int columnId, String feedUrl)
			throws IOException {
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
	}
}

package rss_dashboard.client.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ObjectParser;

import rss_dashboard.client.network.dashboard.DashboardLayoutRequest;
import rss_dashboard.client.network.dashboard.DashboardLayoutResponse;
import rss_dashboard.client.network.dashboard.DashboardModificationRequest;
import rss_dashboard.client.network.dashboard.DashboardModificationResponse;
import rss_dashboard.client.network.dashboard.DashboardRequest;
import rss_dashboard.client.network.dashboard.DashboardResponse;
import rss_dashboard.client.network.misc.KeepAliveRequest;
import rss_dashboard.client.network.misc.KeepAliveResponse;
import rss_dashboard.client.network.misc.LoginRequest;
import rss_dashboard.client.network.misc.LoginResponse;
import rss_dashboard.client.network.misc.LogoutRequest;
import rss_dashboard.client.network.rss.RssChannelRequest;
import rss_dashboard.client.network.rss.RssChannelResponse;
import rss_dashboard.client.network.rss.RssItemRequest;
import rss_dashboard.client.network.rss.RssItemResponse;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.common.network.dashboard.IDashboardLayoutRequest;
import rss_dashboard.common.network.dashboard.IDashboardLayoutResponse;
import rss_dashboard.common.network.dashboard.IDashboardModificationRequest;
import rss_dashboard.common.network.dashboard.IDashboardRequest;
import rss_dashboard.common.network.dashboard.IDashboardResponse;
import rss_dashboard.common.network.misc.IKeepAliveRequest;
import rss_dashboard.common.network.misc.IKeepAliveResponse;
import rss_dashboard.common.network.misc.ILoginRequest;
import rss_dashboard.common.network.misc.ILoginResponse;
import rss_dashboard.common.network.misc.ILogoutRequest;
import rss_dashboard.common.network.rss.IRssChannelRequest;
import rss_dashboard.common.network.rss.IRssChannelResponse;
import rss_dashboard.common.network.rss.IRssItemRequest;
import rss_dashboard.common.network.rss.IRssItemResponse;

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
		ILoginRequest request = LoginRequest.builder().username(username).password(password).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"POST", loginUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		ILoginResponse response = httpResponse.parseAs(LoginResponse.class);
		httpResponse.disconnect();
		return response.getToken();
	}

	@Override
	public void logout(String token) throws IOException {
		ILogoutRequest request = LogoutRequest.builder().token(token).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"POST", logoutUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		httpResponse.disconnect();
	}

	@Override
	public boolean keepAlive(String token) throws IOException {
		IKeepAliveRequest request = KeepAliveRequest.builder().token(token).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"POST", keepAliveUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		IKeepAliveResponse response = httpResponse.parseAs(KeepAliveResponse.class);
		httpResponse.disconnect();
		return response.isAlive();
	}

	@Override
	public Map<String, IRssChannel> getRssChannels(String token, List<String> ids) throws IOException {
		IRssChannelRequest request = RssChannelRequest.builder().token(token).ids(ids).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"GET", rssChannelUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		IRssChannelResponse response = httpResponse.parseAs(RssChannelResponse.class);
		httpResponse.disconnect();
		return response.getChannels();
	}

	@Override
	public Map<String, IRssItem> getRssItems(String token, List<String> ids) throws IOException {
		IRssItemRequest request = RssItemRequest.builder().token(token).ids(ids).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"GET", rssItemUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		IRssItemResponse response = httpResponse.parseAs(RssItemResponse.class);
		httpResponse.disconnect();
		return response.getItems();
	}

	@Override
	public IDashboard getDashboard(String token) throws IOException {
		IDashboardRequest request = DashboardRequest.builder().token(token).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"GET", dashboardUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		IDashboardResponse response = httpResponse.parseAs(DashboardResponse.class);
		httpResponse.disconnect();
		return response.getDashboard();
	}

	@Override
	public IDashboardLayout getDashboardLayout(String token) throws IOException {
		IDashboardLayoutRequest request = DashboardLayoutRequest.builder().token(token).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"GET", dashboardLayoutUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		IDashboardLayoutResponse response = httpResponse.parseAs(DashboardLayoutResponse.class);
		httpResponse.disconnect();
		return response.getLayout();
	}

	@Override
	public void addFeedUrl(String token, int pageId, int rowId, int columnId, String feedUrl) throws IOException {
		IDashboardModificationRequest request = DashboardModificationRequest.builder()
				.pageId(pageId).rowId(rowId).columnId(columnId).feedUrl(feedUrl).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"POST", dashboardUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		httpResponse.parseAs(DashboardModificationResponse.class);
		httpResponse.disconnect();
	}

	@Override
	public void removeFeedUrl(String token, int pageId, int rowId, int columnId) throws IOException {
		IDashboardModificationRequest request = DashboardModificationRequest.builder()
				.pageId(pageId).rowId(rowId).columnId(columnId).feedUrl(null).build();
		HttpRequest httpRequest = HTTP_REQUEST_FACTORY.buildRequest(
				"DELETE", dashboardUrl, new JsonHttpContent(JSON_FACTORY, request));
		HttpResponse httpResponse = httpRequest.execute();
		httpResponse.parseAs(DashboardModificationResponse.class);
		httpResponse.disconnect();
	}
}

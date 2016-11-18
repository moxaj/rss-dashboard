package rss_dashboard.client.network;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;

public interface INetworkClient {
	String login(String username, String password) throws IOException;

	void logout(String token) throws IOException;

	boolean keepAlive(String token) throws IOException;

	Map<String, IRssChannel> getRssChannels(String token, List<String> ids) throws IOException;

	Map<String, IRssItem> getRssItems(String token, List<String> ids) throws IOException;

	IDashboard getDashboard(String token) throws IOException;

	IDashboardLayout getDashboardLayout(String token) throws IOException;

	void addFeedUrl(String token, int pageId, int rowId, int columnId, String feedUrl) throws IOException;

	void removeFeedUrl(String token, int pageId, int rowId, int columnId) throws IOException;
}

package rss_dashboard.client.network;

import java.io.IOException;
import java.util.List;

import rss_dashboard.common.model.dashboard.IDashboardMapping;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;

public interface INetworkAuthenticatedClient {
	void logout(String token) throws IOException;

	boolean doKeepAlive(String token) throws IOException;

	IRssChannel getRssChannel(String token, String id) throws IOException;

	IRssItem getRssItem(String token, String id) throws IOException;

	IDashboardMapping getDashboardMapping(String token, long dateFrom, long dateTill, List<String> categories) throws IOException;

	IDashboardLayout getDashboardLayout(String token) throws IOException;

	void modifyDashboardLayout(String token, int pageId, int rowId, int columnId, String feedUrl) throws IOException;
}

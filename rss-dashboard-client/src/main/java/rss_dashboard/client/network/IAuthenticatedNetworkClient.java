package rss_dashboard.client.network;

import java.util.concurrent.CompletableFuture;

import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public interface IAuthenticatedNetworkClient {
	CompletableFuture<Void> logout(String token);

	CompletableFuture<IRssChannel> getRssChannel(String token, String id);

	CompletableFuture<IRssItem> getRssItem(String token, String id);

	CompletableFuture<IRssChannelMapping> getRssChannelMapping(String token, String id);

	CompletableFuture<IDashboard> getDashboard(String token);

	CompletableFuture<Void> modifyDashboardLayout(String token, int pageId, int rowId, int columnId, String feedUrl);
}

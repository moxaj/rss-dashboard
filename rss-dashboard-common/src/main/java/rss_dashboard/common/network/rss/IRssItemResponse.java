package rss_dashboard.common.network.rss;

import java.util.Map;

import rss_dashboard.common.model.rss.IRssItem;

public interface IRssItemResponse {
	Map<String, IRssItem> getItems();
}

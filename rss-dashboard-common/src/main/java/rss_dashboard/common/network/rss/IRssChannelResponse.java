package rss_dashboard.common.network.rss;

import java.util.Map;

import rss_dashboard.common.model.rss.IRssChannel;

public interface IRssChannelResponse {
	Map<String, IRssChannel> getChannels();
}
	
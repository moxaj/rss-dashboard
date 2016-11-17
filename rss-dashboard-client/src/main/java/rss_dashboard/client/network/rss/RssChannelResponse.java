package rss_dashboard.client.network.rss;

import java.util.Map;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.network.rss.IRssChannelResponse;

@Getter
@Builder
public class RssChannelResponse implements IRssChannelResponse {
	@Key
	private final Map<String, IRssChannel> channels;
}

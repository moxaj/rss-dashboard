package rss_dashboard.client.network.rss;

import java.util.Map;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.common.network.rss.IRssItemResponse;

@Getter
@Builder
public class RssItemResponse implements IRssItemResponse {
	@Key
	private final Map<String, IRssItem> items;
}

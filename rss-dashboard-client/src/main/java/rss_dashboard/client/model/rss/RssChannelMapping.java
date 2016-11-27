package rss_dashboard.client.model.rss;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.rss.IRssChannelMapping;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RssChannelMapping implements IRssChannelMapping {
	private List<String> rssItemIds;
}

package rss_dashboard.client.model.rss;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssItem;

@Getter
public class RssItem extends RssElement implements IRssItem {
	@Key
	private final String author;

	@Builder
	private RssItem(String id, String title, String link, String description, List<String> categories, String pubDate,
			String author) {
		super(id, title, link, description, categories, pubDate);
		this.author = author;
	}
}

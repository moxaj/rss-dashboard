package rss_dashboard.client.model.rss.impl;

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
	public RssItem(String id, String title, String link, String description, List<String> categories, String pubDate,
			byte[] image, String author) {
		super(id, title, link, description, categories, pubDate, image);
		this.author = author;
	}
}

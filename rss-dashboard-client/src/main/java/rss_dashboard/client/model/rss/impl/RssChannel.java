package rss_dashboard.client.model.rss.impl;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssChannel;

@Getter
public class RssChannel extends RssElement implements IRssChannel {
	@Key
	private final String language;

	@Builder
	private RssChannel(String id, String title, String link, String description, List<String> categories,
			String pubDate, byte[] image, String language) {
		super(id, title, link, description, categories, pubDate, image);
		this.language = language;
	}
}

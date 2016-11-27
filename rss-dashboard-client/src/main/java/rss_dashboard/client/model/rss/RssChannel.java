package rss_dashboard.client.model.rss;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.rss.IRssChannel;

@Getter
@NoArgsConstructor
public class RssChannel extends RssElement implements IRssChannel {
	@Key
	private String language;
	@Key
	private String imageUrl;

	@Builder
	private RssChannel(String id, String title, String link, String description, List<String> categories,
			String pubDate, String language, String imageUrl) {
		super(id, title, link, description, categories, pubDate);
		this.language = language;
		this.imageUrl = imageUrl;
	}
}

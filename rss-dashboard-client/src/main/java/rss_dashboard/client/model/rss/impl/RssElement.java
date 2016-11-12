package rss_dashboard.client.model.rss.impl;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssElement;

@AllArgsConstructor
@Getter
public class RssElement implements IRssElement {
	@Key
	private final String id;
	@Key
	private final String title;
	@Key
	private final String link;
	@Key
	private final String description;
	@Key
	private final List<String> categories;
	@Key
	private final String pubDate;
	@Key
	private final byte[] image;
}

package rss_dashboard.common.model.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.IRssItem;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class RssItem extends RssElement implements IRssItem {
	private final String author;

	@Builder
	public RssItem(String title, String link, String description, List<String> categories, String pubDate, byte[] image,
			String author) {
		super(title, link, description, categories, pubDate, image);
		this.author = author;
	}
}

package rss_dashboard.common.model.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.IRssChannel;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class RssChannel extends RssElement implements IRssChannel {
	private final String language;

	@Builder
	private RssChannel(String title, String link, String description, List<String> categories, String pubDate,
			byte[] image, String language) {
		super(title, link, description, categories, pubDate, image);
		this.language = language;
	}
}

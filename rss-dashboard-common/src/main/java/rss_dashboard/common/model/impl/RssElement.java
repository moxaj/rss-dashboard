package rss_dashboard.common.model.impl;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rss_dashboard.common.model.IRssElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@AllArgsConstructor
@Getter
public class RssElement implements IRssElement {
	private final String title;
	private final String link;
	private final String description;
	private final List<String> categories;
	private final String pubDate;
	private final byte[] image;
}

package rss_dashboard.server.model.rss;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.rss.IRssChannel;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class RssChannel extends RssElement implements IRssChannel {
	private String language;
	private String imageUrl;

	@Builder
	private RssChannel(String id, String title, String link, String description, List<String> categories,
			LocalDate pubDate, String language, String imageUrl) {
		super(id, title, link, description, categories, pubDate);
		this.language = language;
		this.imageUrl = imageUrl;
	}
}

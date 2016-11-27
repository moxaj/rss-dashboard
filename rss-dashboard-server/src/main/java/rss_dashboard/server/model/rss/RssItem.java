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
import rss_dashboard.common.model.rss.IRssItem;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Setter
@NoArgsConstructor
public class RssItem extends RssElement implements IRssItem {
	private String author;

	@Builder
	private RssItem(String id, String title, String link, String description, List<String> categories,
			LocalDate pubDate, String author) {
		super(id, title, link, description, categories, pubDate);
		this.author = author;
	}
}
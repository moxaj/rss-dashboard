package rss_dashboard.server.model.rss;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.rss.IRssElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class RssElement implements IRssElement {
	private String id;
	private String title;
	private String link;
	private String description;
	private List<String> categories;
	private String pubDate;
}

package rss_dashboard.client.model.rss;

import java.time.LocalDate;
import java.util.List;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.rss.IRssElement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RssElement implements IRssElement {
	@Key
	private String id;
	@Key
	private String title;
	@Key
	private String link;
	@Key
	private String description;
	@Key
	private List<String> categories;
	@Key
	private LocalDate pubDate;
}

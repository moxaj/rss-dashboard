package rss_dashboard.common.model.rss;

import java.time.LocalDate;
import java.util.List;

public interface IRssElement {
	String getId();

	String getTitle();

	String getLink();

	String getDescription();

	List<String> getCategories();

	LocalDate getPubDate();
}

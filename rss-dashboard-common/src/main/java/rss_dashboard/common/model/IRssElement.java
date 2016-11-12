package rss_dashboard.common.model;

import java.util.List;

public interface IRssElement {
	String getTitle();

	String getLink();

	String getDescription();

	List<String> getCategories();

	String getPubDate();
	
	byte[] getImage();
}

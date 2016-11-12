package rss_dashboard.common.model.dashboard;

import java.util.List;

public interface IDashboardFilter {
	List<String> getKeywords();

	String getFromPubDate();

	String getToPubDate();
}

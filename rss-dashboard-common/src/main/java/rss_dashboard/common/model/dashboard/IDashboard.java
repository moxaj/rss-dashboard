package rss_dashboard.common.model.dashboard;

import java.util.List;
import java.util.Map;

public interface IDashboard {
	int getWidth();

	int getHeight();

	List<Map<String, Position>> getLayout();
}

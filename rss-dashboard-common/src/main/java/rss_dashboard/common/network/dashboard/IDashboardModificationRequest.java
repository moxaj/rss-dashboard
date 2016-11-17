package rss_dashboard.common.network.dashboard;

import rss_dashboard.common.network.IAuthenticatedRequest;

public interface IDashboardModificationRequest extends IAuthenticatedRequest {
	int getPageId();

	int getRowId();

	int getColumnId();

	String getFeedUrl();
}

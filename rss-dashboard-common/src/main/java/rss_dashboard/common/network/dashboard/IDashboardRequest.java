package rss_dashboard.common.network.dashboard;

import rss_dashboard.common.model.dashboard.IDashboardFilter;
import rss_dashboard.common.network.IAuthenticatedRequest;

public interface IDashboardRequest extends IAuthenticatedRequest {
	IDashboardFilter getFilter();
}

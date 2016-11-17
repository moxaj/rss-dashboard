package rss_dashboard.client.network.dashboard;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.network.dashboard.IDashboardResponse;

@Getter
@Builder
public class DashboardResponse implements IDashboardResponse {
	@Key
	private final IDashboard dashboard;
}

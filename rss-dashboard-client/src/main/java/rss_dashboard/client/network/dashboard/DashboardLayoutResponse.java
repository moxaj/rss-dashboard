package rss_dashboard.client.network.dashboard;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.network.dashboard.IDashboardLayoutResponse;

@Getter
@Builder
public class DashboardLayoutResponse implements IDashboardLayoutResponse {
	@Key
	private final IDashboardLayout layout;
}

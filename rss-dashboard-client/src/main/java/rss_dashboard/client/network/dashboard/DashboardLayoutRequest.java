package rss_dashboard.client.network.dashboard;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.network.dashboard.IDashboardLayoutRequest;

@Getter
public class DashboardLayoutRequest extends AuthenticatedRequest implements IDashboardLayoutRequest {
	@Builder
	public DashboardLayoutRequest(String token) {
		super(token);
	}
}

package rss_dashboard.client.network.dashboard;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.model.dashboard.IDashboardFilter;
import rss_dashboard.common.network.dashboard.IDashboardRequest;

@Getter
public class DashboardRequest extends AuthenticatedRequest implements IDashboardRequest {
	@Key
	private final IDashboardFilter filter;

	@Builder
	private DashboardRequest(String token, IDashboardFilter filter) {
		super(token);
		this.filter = filter;
	}
}

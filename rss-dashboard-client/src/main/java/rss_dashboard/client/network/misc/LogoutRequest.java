package rss_dashboard.client.network.misc;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.network.misc.ILogoutRequest;

@Getter
public class LogoutRequest extends AuthenticatedRequest implements ILogoutRequest {
	@Builder
	private LogoutRequest(String token) {
		super(token);
	}
}

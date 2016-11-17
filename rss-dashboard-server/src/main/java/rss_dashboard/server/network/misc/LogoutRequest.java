package rss_dashboard.server.network.misc;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.ILogoutRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@Getter
public class LogoutRequest extends AuthenticatedRequest implements ILogoutRequest {
	@Builder
	private LogoutRequest(String token) {
		super(token);
	}
}

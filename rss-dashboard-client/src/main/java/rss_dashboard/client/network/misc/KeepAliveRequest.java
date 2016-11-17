package rss_dashboard.client.network.misc;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.network.misc.IKeepAliveRequest;

@Getter
public class KeepAliveRequest extends AuthenticatedRequest implements IKeepAliveRequest {
	@Builder
	public KeepAliveRequest(String token) {
		super(token);
	}
}

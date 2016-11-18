package rss_dashboard.client.network.rss;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.network.rss.IRssChannelRequest;

@Getter
public class RssChannelRequest extends AuthenticatedRequest implements IRssChannelRequest {
	@Key
	private final List<String> ids;

	@Builder
	private RssChannelRequest(String token, List<String> ids) {
		super(token);
		this.ids = ids;
	}
}

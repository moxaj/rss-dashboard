package rss_dashboard.client.network.misc;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.IKeepAliveResponse;

@Getter
@Builder
public class KeepAliveResponse implements IKeepAliveResponse {
	@Key
	private final boolean alive;
}

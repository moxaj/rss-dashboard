package rss_dashboard.client.network;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rss_dashboard.common.network.IAuthenticatedRequest;

@AllArgsConstructor
@Getter
public class AuthenticatedRequest implements IAuthenticatedRequest {
	@Key
	private final String token;
}

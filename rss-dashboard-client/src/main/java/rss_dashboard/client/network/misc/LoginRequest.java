package rss_dashboard.client.network.misc;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.ILoginRequest;

@Getter
@Builder
public class LoginRequest implements ILoginRequest {
	@Key
	private final String username;
	@Key
	private final String password;
}

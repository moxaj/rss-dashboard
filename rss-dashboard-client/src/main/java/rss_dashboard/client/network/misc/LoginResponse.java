package rss_dashboard.client.network.misc;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.ILoginResponse;

@Getter
@Builder
public class LoginResponse implements ILoginResponse {
	@Key
	private final String token;
}

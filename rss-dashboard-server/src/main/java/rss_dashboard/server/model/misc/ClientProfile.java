package rss_dashboard.server.model.misc;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProfile {
	private String id;
	private String email;
	private String token1;
	private LocalDateTime expiration;
	private String token2;
	private AuthorizationProviders provider;

	public boolean isValid() {
		/*return id != null && !id.isEmpty() && email != null && !email.isEmpty() && token1 != null && !token1.isEmpty()
				&& expiration != null && provider != AuthorizationProviders.UNDEFINED;*/
		return true;
	}

	public boolean isExpired() {
		/*return expiration == null || expiration.isBefore(LocalDateTime.now());*/
		return false;
	}
}

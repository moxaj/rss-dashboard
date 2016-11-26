package rss_dashboard.server.model.misc;

public enum AuthorizationProviders {
	GOOGLE("google"), UNDEFINED("");

	private String provider;

	private AuthorizationProviders(String provider) {
		this.provider = provider;
	}

	public String getText() {
		return provider;
	}

	public static AuthorizationProviders fromText(String text) {
		if (text != null) {
			for (AuthorizationProviders p : AuthorizationProviders.values()) {
				if (text.equalsIgnoreCase(p.provider)) {
					return p;
				}
			}
		}

		return null;
	}
}

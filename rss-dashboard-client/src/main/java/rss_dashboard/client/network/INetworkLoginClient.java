package rss_dashboard.client.network;

import java.io.IOException;

public interface INetworkLoginClient {
	String login(String username, String password) throws IOException;
}

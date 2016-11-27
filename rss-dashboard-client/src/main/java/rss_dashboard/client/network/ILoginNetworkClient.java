package rss_dashboard.client.network;

import java.util.concurrent.CompletableFuture;

public interface ILoginNetworkClient {
	CompletableFuture<String> login(String username, String password);
}

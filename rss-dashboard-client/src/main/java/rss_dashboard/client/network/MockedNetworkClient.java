package rss_dashboard.client.network;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import rss_dashboard.client.model.dashboard.Dashboard;
import rss_dashboard.client.model.rss.RssChannel;
import rss_dashboard.client.model.rss.RssChannelMapping;
import rss_dashboard.client.model.rss.RssItem;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.Position;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public class MockedNetworkClient implements INetworkClient {
	public MockedNetworkClient() throws MalformedURLException {

	}

	private void sleep() {
		try {
			Thread.sleep((long) (Math.random() * 200));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompletableFuture<String> login(String username, String password) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return "dummy_token";
		});
	}

	@Override
	public CompletableFuture<Void> logout(String token) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return null;
		});
	}

	@Override
	public CompletableFuture<Boolean> doKeepAlive(String token) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return true;
		});
	}

	@Override
	public CompletableFuture<IRssChannel> getRssChannel(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			return RssChannel.builder()
					.id(id)
					.title("Index.hu")
					.description("very very index")
					.categories(Arrays.asList(new String[] { "BULVÁR" }))
					.pubDate("2016.12.01")
					.link("www.index.hu")
					.language("HU")
					.imageUrl(null)
					.build();
		});
	}

	@Override
	public CompletableFuture<IRssItem> getRssItem(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			return RssItem.builder()
					.id("Z")
					.title("Afghan forces what what what bla bla")
					.description("According to the blah blah, the forces of blublu blub lubblublu")
					.categories(Arrays.asList(new String[] { "Közélet", "BuLVáR" }))
					.pubDate("2016.08.29")
					.link("www.itemlink.hu")
					.author("item author")
					.build();
		});
	}

	@Override
	public CompletableFuture<IRssChannelMapping> getRssChannelMapping(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			return RssChannelMapping.builder().rssItemIds(Arrays.asList(new String[] {
					"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
			})).build();
		});
	}

	@Override
	public CompletableFuture<IDashboard> getDashboard(String token) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			List<Map<String, Position>> layout = new ArrayList<>();
			Map<String, Position> page = new HashMap<>();
			page.put("1", new Position(0, 0, 50, 50));
			page.put("2", new Position(50, 20, 50, 50));
			layout.add(page);
			// TODO
			return Dashboard.builder().layout(layout).width(100).height(100).build();
		});
	}

	@Override
	public CompletableFuture<Void> modifyDashboardLayout(String token, int pageId, int rowId, int columnId,
			String feedUrl) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return null;
		});
	}
}

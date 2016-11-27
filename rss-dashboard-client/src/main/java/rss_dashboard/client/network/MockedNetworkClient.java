package rss_dashboard.client.network;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rss_dashboard.client.model.dashboard.Dashboard;
import rss_dashboard.client.model.rss.RssChannel;
import rss_dashboard.client.model.rss.RssChannelMapping;
import rss_dashboard.client.model.rss.RssItem;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public class MockedNetworkClient implements INetworkClient {
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(64);

	public MockedNetworkClient() throws MalformedURLException {

	}

	private void sleep() {
		try {
			Thread.sleep((long) (0 + Math.random() * 200));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompletableFuture<String> login(String username, String password) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return "dummy_token";
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<Void> logout(String token) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return null;
		}, EXECUTOR);
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
					.pubDate(LocalDate.parse("2016-08-29"))
					.link("www.index.hu")
					.language("HU")
					.imageUrl(null)
					.build();
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IRssItem> getRssItem(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			String[][] categories = new String[][] {
					{ "a", "b", "c" },
					{ "a", "b", "d" },
					{ "a", "c", "c" },
					{ "a", "c", "d" },
					{ "a", "c", "a" },
					{ "a", "c", "f" },
					{ "a", "e", "c" }
			};

			if (Math.random() < 0.5) {
				return RssItem.builder()
						.id("Z")
						.title("Afghan forces what what what bla bla")
						.description("According to the blah blah, the forces of blublu blub lubblublu")
						.categories(Arrays.asList(categories[(int) (Math.random() * 7)]))
						.pubDate(LocalDate.parse("2016-08-29"))
						.link("www.itemlink.hu")
						.author("item author")
						.build();
			} else {
				return RssItem.builder()
						.id("Z")
						.title("Holy paladins")
						.description("According to the blah blah, the forces of blublu blub lubblublu")
						.categories(Arrays.asList(categories[(int) (Math.random() * 7)]))
						.pubDate(LocalDate.parse("2016-08-29"))
						.link("www.itemlink.hu")
						.author("item author")
						.build();
			}
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IRssChannelMapping> getRssChannelMapping(String token, String id) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();

			String[] rssItemIds = new String[40];
			for (int i = 0; i < 40; i++) {
				rssItemIds[i] = Integer.toString(i);
			}

			return RssChannelMapping.builder().rssItemIds(Arrays.asList(rssItemIds)).build();
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<IDashboard> getDashboard(String token) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return Dashboard.builder().layout(Arrays.asList(new String[][][] {
					{ { null, null }, { "3", "4" }, { "5", null } },
					{ { "7", "8" }, { "9", "10" }, { "11", "12" } },
					{ { "70", "80" }, { "90", "100" }, { "110", "120" } },
					{ { "71", "81" }, { "91", "101" }, { "111", "121" } }
			})).build();
		}, EXECUTOR);
	}

	@Override
	public CompletableFuture<Void> modifyDashboardLayout(String token, int pageId, int rowId, int columnId,
			String feedUrl) {
		return CompletableFuture.supplyAsync(() -> {
			sleep();
			return null;
		}, EXECUTOR);
	}

	@Override
	public void shutdown() {
		EXECUTOR.shutdown();
	}
}

package rss_dashboard.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rss_dashboard.client.network.INetworkClient;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.Position;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public class MainController extends AbstractController {
	private static final int CACHE_TIMER_PERIOD_MS = 600000;

	@FXML
	private TabPane rssTabPane;
	@FXML
	private TextField keywordsTextField;
	@FXML
	private TreeView<String> categoryTreeView;

	// Model
	private final Map<String, IRssChannel> rssChannels = new ConcurrentHashMap<>();
	private final Map<String, IRssItem> rssItems = new ConcurrentHashMap<>();
	private final Map<String, List<String>> rssChannelMappings = new ConcurrentHashMap<>();
	private IDashboard dashboard;

	private final Map<String, RssChannelController> rssChannelControllers = new ConcurrentHashMap<>();
	private final Map<String, RssItemController> rssItemControllers = new ConcurrentHashMap<>();
	private final Timer cacheTimer = new Timer();

	private INetworkClient networkClient;
	private String token;
	private boolean promptLogin = false;

	public void setNetworkClient(INetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean doPromptLogin() {
		return promptLogin;
	}

	private CompletableFuture<IRssChannel> loadRssChannelAsync(String rssChannelId) {
		return networkClient.getRssChannel(token, rssChannelId)
				.thenApplyAsync(rssChannel -> {
					rssChannels.put(rssChannelId, rssChannel);
					return rssChannel;
				})
				.exceptionally(ex -> {
					Alerts.showServerUnavailableAlert();
					close(true);
					return null;
				});
	}

	private CompletableFuture<IRssItem> loadRssItemAsync(String rssItemId) {
		return networkClient.getRssItem(token, rssItemId)
				.thenApplyAsync(rssItem -> {
					rssItems.put(rssItemId, rssItem);
					return rssItem;
				})
				.exceptionally(ex -> {
					Alerts.showServerUnavailableAlert();
					close(true);
					return null;
				});
	}

	private CompletableFuture<IDashboard> loadDashboardAsync() {
		return networkClient.getDashboard(token)
				.thenApplyAsync(dashboard -> {
					this.dashboard = dashboard;
					return dashboard;
				})
				.exceptionally(ex -> {
					Alerts.showServerUnavailableAlert();
					close(true);
					return null;
				});
	}

	private CompletableFuture<IRssChannelMapping> loadRssChannelMappingAsync(String rssChannelId) {
		return networkClient.getRssChannelMapping(token, rssChannelId)
				.thenApplyAsync(rssChannelMapping -> {
					rssChannelMappings.put(rssChannelId, rssChannelMapping.getRssItemIds());
					return rssChannelMapping;
				})
				.exceptionally(ex -> {
					Alerts.showServerUnavailableAlert();
					close(true);
					return null;
				});
	}

	private void close(boolean promptLogin) {
		this.promptLogin = promptLogin;
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private void invalidateCaches() {
		rssChannels.clear();
		rssItems.clear();
	}

	private void setupCacheTimer() {
		cacheTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				invalidateCaches();
			}
		}, 0, CACHE_TIMER_PERIOD_MS);
	}

	private void renderCategories(String rssItemId) {
		List<String> categories = rssItems.get(rssItemId).getCategories();

		TreeItem<String> item = new TreeItem<String>("Categories");
		categoryTreeView.setRoot(item);

		while (!categories.isEmpty()) {
			TreeItem<String> nextItem = null;
			for (TreeItem<String> childItem : item.getChildren()) {
				if (childItem.getValue().equals(categories.get(0))) {
					nextItem = childItem;
					break;
				}
			}

			if (nextItem == null) {
				nextItem = new TreeItem<>(categories.get(0));
				item.getChildren().add(nextItem);
			}

			item = nextItem;
			categories = categories.subList(1, categories.size());
		}
	}

	private void renderRssChannel(String rssChannelId, int page, Position position) {
		IRssChannel rssChannel = rssChannels.get(rssChannelId);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RssChannelView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		RssChannelController controller = loader.getController();
		rssChannelControllers.put(rssChannel.getId(), controller);
		controller.setRssChannel(rssChannel);

		Tab rssTab = rssTabPane.getTabs().get(page);
		GridPane rssGridPane = (GridPane) rssTab.getContent();
		rssGridPane.add(pane, position.left, position.top, position.width, position.height);
	}

	private void renderLayout(IDashboard dashboard) {
		int width = dashboard.getWidth();
		int height = dashboard.getHeight();

		for (int i = 0; i < dashboard.getLayout().size(); i++) {
			Tab rssTab = new Tab("Page" + (i + 1));
			rssTab.setClosable(false);
			rssTabPane.getTabs().add(rssTab);

			GridPane rssGridPane = new GridPane();
			List<ColumnConstraints> columnConstraints = new ArrayList<>();
			for (int j = 0; j < width; j++) {
				ColumnConstraints constraint = new ColumnConstraints();
				constraint.setPercentWidth(100.0 / width);
				columnConstraints.add(constraint);
			}

			rssGridPane.getColumnConstraints().addAll(columnConstraints);
			List<RowConstraints> rowConstraints = new ArrayList<>();
			for (int j = 0; j < height; j++) {
				RowConstraints constraint = new RowConstraints();
				constraint.setPercentHeight(100.0 / height);
				rowConstraints.add(constraint);
			}

			rssGridPane.getRowConstraints().addAll(rowConstraints);
			rssTab.setContent(rssGridPane);
		}
	}

	private void renderMapping(String rssChannelId, List<String> categories, List<String> keywords) {
		rssChannelControllers.get(rssChannelId).setRssItems(
				rssChannelMappings.get(rssChannelId)
						.stream()
						.map(rssItems::get)
						.filter(rssItem -> {
							return (categories.isEmpty()
									|| categories.stream().anyMatch(category -> {
										return rssItem.getCategories().stream().anyMatch(category::equalsIgnoreCase);
									})) && (keywords.isEmpty() || keywords.stream().anyMatch(keyword -> {
										return rssItem.getDescription().toLowerCase().contains(keyword.toLowerCase())
												|| rssItem.getTitle().toLowerCase().contains(keyword.toLowerCase());
									}));
						})
						.collect(Collectors.toList()));
	}

	public void shutdown() {
		cacheTimer.cancel();
	}

	public void load() {
		setupCacheTimer();

		runTask(loadDashboardAsync().thenApplyAsync(dashboard -> {
			renderLayout(dashboard);
			return dashboard;
		}, Platform::runLater).thenComposeAsync(dashboard -> {
			List<CompletableFuture<?>> futures = new ArrayList<>();

			List<Map<String, Position>> layout = dashboard.getLayout();
			for (int i = 0; i < layout.size(); i++) {
				final int k = i;
				layout.get(i).forEach((rssChannelId, position) -> {
					futures.add(loadRssChannelAsync(rssChannelId).thenComposeAsync(rssChannel -> {
						renderRssChannel(rssChannelId, k, position);
						return loadRssChannelMappingAsync(rssChannelId).thenComposeAsync(rssChannelMapping -> {
							return CompletableFuture.allOf(rssChannelMapping.getRssItemIds()
									.stream()
									.map(rssItemId -> {
										return loadRssItemAsync(rssItemId).thenRunAsync(() -> {
											renderCategories(rssItemId);
										}, Platform::runLater);
									})
									.collect(Collectors.toList())
									.toArray(new CompletableFuture<?>[0]))
									.thenRunAsync(() -> {
										renderMapping(rssChannelId, new ArrayList<>(), new ArrayList<>());
									}, Platform::runLater);
						}, Platform::runLater);
					}, Platform::runLater));
				});
			}

			return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
		}));
	}

	@FXML
	public void initialize() {
		categoryTreeView.setShowRoot(false);
		categoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		categoryTreeView.getSelectionModel().selectedItemProperty().addListener(any -> {
			applyFilter();
		});
	}

	private void applyFilter() {
		rssChannels.keySet().forEach(rssChannelId -> {
			renderMapping(
					rssChannelId,
					categoryTreeView.getSelectionModel().getSelectedItems()
							.stream()
							.map(TreeItem::getValue)
							.collect(Collectors.toList()),
					Arrays.asList(keywordsTextField.getText().split(" ")));
		});
	}

	@FXML
	public void handleKeywordsKeyPressed(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ENTER) {
			applyFilter();
		} else if (code == KeyCode.ESCAPE) {
			keywordsTextField.clear();
			applyFilter();
		}
	}

	@FXML
	public void handleCategoriesKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			categoryTreeView.getSelectionModel().clearSelection();
			applyFilter();
		}
	}
}

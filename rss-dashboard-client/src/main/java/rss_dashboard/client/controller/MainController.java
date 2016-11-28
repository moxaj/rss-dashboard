package rss_dashboard.client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.SynthScrollBarUI;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import rss_dashboard.client.network.IAuthenticatedNetworkClient;
import rss_dashboard.client.network.INetworkClient;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;

public class MainController extends AbstractController {
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
	private final Map<String, RssChannelController> rssChannelControllers = new ConcurrentHashMap<>();

	private IAuthenticatedNetworkClient networkClient;
	private HostServices hostServices;
	private String token;
	private boolean promptLogin = false;

	public void setNetworkClient(INetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean doPromptLogin() {
		return promptLogin;
	}

	// Base

	private CompletableFuture<IRssChannel> loadRssChannelAsync(String rssChannelId) {
		return networkClient.getRssChannel(token, rssChannelId)
				.thenApplyAsync(rssChannel -> {
					rssChannels.put(rssChannelId, rssChannel);
					return rssChannel;
				})
				.exceptionally(ex -> {
					Platform.runLater(() -> {
						Alerts.showServerUnavailableAlert();
						close(true);
					});

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
					Platform.runLater(() -> {
						Alerts.showServerUnavailableAlert();
						close(true);
					});

					return null;
				});
	}

	private CompletableFuture<IDashboard> loadDashboardAsync() {
		return networkClient.getDashboard(token)
				.exceptionally(ex -> {
					Platform.runLater(() -> {
						Alerts.showServerUnavailableAlert();
						close(true);
					});

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
					Platform.runLater(() -> {
						Alerts.showServerUnavailableAlert();
						close(true);
					});

					return null;
				});
	}

	private CompletableFuture<Void> saveDashboardModificationAsync(int page, int row, int column, String url) {
		return networkClient
				.modifyDashboardLayout(token, page, row, column, url)
				.exceptionally(ex -> {
					Platform.runLater(() -> {
						Alerts.showServerUnavailableAlert();
						close(true);
					});

					return null;
				});
	}

	// Render

	private void renderCategories() {
		categoryTreeView.setRoot(new TreeItem<String>("Categories"));

		rssChannelMappings.values().forEach(rssItemIds -> {
			rssItemIds.stream().map(rssItems::get).forEach(rssItem -> {
				List<String> categories = rssItem.getCategories();
				TreeItem<String> item = categoryTreeView.getRoot();

				while (!categories.isEmpty()) {
					TreeItem<String> nextItem = null;
					for (TreeItem<String> childItem : item.getChildren()) {
						if (childItem.getValue().equalsIgnoreCase(categories.get(0))) {
							nextItem = childItem;
							break;
						}
					}

					if (nextItem == null) {
						String category = categories.get(0);
						nextItem = new TreeItem<>(category);

						int index = 0;
						List<TreeItem<String>> children = item.getChildren();
						for (; index < children.size(); index++) {
							if (children.get(index).getValue().toLowerCase()
									.compareTo(category.toLowerCase()) > 0) {
								break;
							}
						}

						if (index == children.size()) {
							children.add(nextItem);
						} else {
							children.add(null);
							for (int i = children.size() - 1; i > index; i--) {
								children.set(i, children.get(i - 1));
							}

							children.set(index, nextItem);
						}
					}

					item = nextItem;
					categories = categories.subList(1, categories.size());
				}
			});
		});
	}

	private void renderRssChannel(int pageId, int rowId, int columnId, String rssChannelId) {
		IRssChannel rssChannel = rssChannels.get(rssChannelId);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RssChannelView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Tab rssTab = rssTabPane.getTabs().get(pageId);
		GridPane rssGridPane = (GridPane) rssTab.getContent();
		rssGridPane.add(pane, columnId, rowId);

		RssChannelController controller = loader.getController();
		rssChannelControllers.put(rssChannelId, controller);
		controller.setDeleteCallback(() -> {
			rssGridPane.getChildren().remove(pane);
			renderEmptyRssChannel(pageId, rowId, columnId);

			rssChannels.remove(rssChannelId);
			rssChannelControllers.remove(rssChannelId);
			rssChannelMappings.remove(rssChannelId);

			queueTask(saveDashboardModificationAsync(pageId, rowId, columnId, null)
					.thenRunAsync(this::renderCategories, Platform::runLater));
		});
		controller.setHostServices(hostServices);
		controller.setRssChannel(rssChannel);
	}

	private void renderEmptyRssChannel(int pageId, int rowId, int columnId) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmptyRssChannelView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		EmptyRssChannelController controller = loader.getController();
		controller.setAddCallback(() -> {
			queueTask(saveDashboardModificationAsync(pageId, rowId, columnId, controller.getUrl())
					.thenComposeAsync(any -> {
						return loadDashboardAsync();
					})
					.thenComposeAsync(dashboard -> {
						return loadRssChannelAndItemsAsync(pageId, rowId, columnId,
								dashboard.getLayout().get(pageId)[rowId][columnId]);
					})
					.thenRunAsync(this::renderCategories, Platform::runLater));
		});

		Tab rssTab = rssTabPane.getTabs().get(pageId);
		GridPane rssGridPane = (GridPane) rssTab.getContent();
		rssGridPane.add(pane, columnId, rowId);
	}

	private void renderDashboard(IDashboard dashboard) {
		List<String[][]> dashboardLayout = dashboard.getLayout();
		int rowCount = dashboardLayout.get(0).length;
		int columnCount = dashboardLayout.get(0)[0].length;

		for (int rowId = 0; rowId < dashboardLayout.size(); rowId++) {
			Tab rssTab = new Tab("" + (rowId + 1));
			rssTab.setClosable(false);
			rssTabPane.getTabs().add(rssTab);

			GridPane rssGridPane = new GridPane();
			rssGridPane.setGridLinesVisible(true);

			List<RowConstraints> rowConstraints = new ArrayList<>();
			for (int j = 0; j < rowCount; j++) {
				RowConstraints constraint = new RowConstraints();
				constraint.setPercentHeight(100.0 / rowCount);
				rowConstraints.add(constraint);
			}
			rssGridPane.getRowConstraints().addAll(rowConstraints);

			List<ColumnConstraints> columnConstraints = new ArrayList<>();
			for (int j = 0; j < columnCount; j++) {
				ColumnConstraints constraint = new ColumnConstraints();
				constraint.setPercentWidth(100.0 / columnCount);
				columnConstraints.add(constraint);
			}
			rssGridPane.getColumnConstraints().addAll(columnConstraints);
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
										return rssItem.getCategories().stream()
												.anyMatch(category::equalsIgnoreCase);
									})) && (keywords.isEmpty() || keywords.stream().anyMatch(keyword -> {
										return rssItem.getDescription().toLowerCase()
												.contains(keyword.toLowerCase())
												|| rssItem.getTitle().toLowerCase().contains(keyword.toLowerCase());
									}));
						})
						.collect(Collectors.toList()));
	}

	// Derived

	private CompletableFuture<Void> loadRssChannelAndItemsAsync(int pageId, int rowId, int columnId,
			String rssChannelId) {
		return loadRssChannelAsync(rssChannelId)
				.thenApplyAsync(rssChannel -> {
					renderRssChannel(pageId, rowId, columnId, rssChannelId);
					return rssChannel;
				}, Platform::runLater)
				.thenComposeAsync(rssChannel -> {
					return loadRssChannelMappingAsync(rssChannelId).thenComposeAsync(rssChannelMapping -> {
						return CompletableFuture.allOf(rssChannelMapping.getRssItemIds()
								.stream()
								.map(rssItemId -> {
									return loadRssItemAsync(rssItemId);
								})
								.collect(Collectors.toList())
								.toArray(new CompletableFuture<?>[0]))
								.thenRunAsync(() -> {
									renderMapping(rssChannelId, new ArrayList<>(), new ArrayList<>());
								}, Platform::runLater);
					});
				})
				.thenRunAsync(this::renderCategories, Platform::runLater);
	}

	public void load() {
		queueTask(loadDashboardAsync().thenApplyAsync(dashboard -> {
			renderDashboard(dashboard);
			return dashboard;
		}, Platform::runLater).thenComposeAsync(dashboard -> {
			List<CompletableFuture<?>> futures = new ArrayList<>();

			List<String[][]> dashboardLayout = dashboard.getLayout();
			for (int pageId = 0; pageId < dashboardLayout.size(); pageId++) {
				String[][] page = dashboardLayout.get(pageId);
				for (int rowId = 0; rowId < page.length; rowId++) {
					String[] row = page[rowId];
					for (int columnId = 0; columnId < row.length; columnId++) {
						String rssChannelId = row[columnId];
						if (rssChannelId != null && rssChannelId.isEmpty()) {
							rssChannelId = null;
						}

						final int u = pageId;
						final int v = rowId;
						final int w = columnId;
						futures.add(rssChannelId == null
								? CompletableFuture.runAsync(() -> {
									renderEmptyRssChannel(u, v, w);
								}, Platform::runLater)
								: loadRssChannelAndItemsAsync(u, v, w, rssChannelId));
					}
				}
			}

			return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
		}));
	}

	// Misc

	private void close(boolean promptLogin) {
		this.promptLogin = promptLogin;
		close();
	}

	private void applyFilter() {
		categoryTreeView.getSelectionModel().getSelectedItems().stream().collect(Collectors.toList());

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
	public void initialize() {
		categoryTreeView.setShowRoot(false);
		categoryTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	@FXML
	public void handleKeywordsKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			keywordsTextField.clear();
		}
	}

	@FXML
	public void handleCategoriesKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			categoryTreeView.getSelectionModel().clearSelection();
		}
	}

	@FXML
	public void handleFilterButtonPressed(ActionEvent event) {
		applyFilter();
	}
}

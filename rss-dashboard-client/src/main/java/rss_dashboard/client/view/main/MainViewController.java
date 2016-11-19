package rss_dashboard.client.view.main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rss_dashboard.client.network.INetworkClient;
import rss_dashboard.client.view.Alerts;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.model.dashboard.IDashboardMapping;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;

public class MainViewController {
	private static final int CACHE_TIMER_PERIOD_MS = 600000;

	@FXML
	private Pane rootPane;
	@FXML
	private TabPane rssTabPane;
	@FXML
	private DatePicker fromDatePicker;
	@FXML
	private DatePicker tillDatePicker;
	@FXML
	private TextField keywordsTextField;

	private final INetworkClient networkClient;
	private final String token;
	private final Map<String, IRssChannel> rssChannels = new HashMap<>();
	private final Map<String, IRssItem> rssItems = new HashMap<>();
	private final Timer cacheTimer = new Timer();
	private boolean promptLogin = false;
	private CompletableFuture<?> currentTask = CompletableFuture.completedFuture(0);

	public MainViewController(INetworkClient networkClient, String token) {
		this.networkClient = networkClient;
		this.token = token;
	}

	public boolean doPromptLogin() {
		return promptLogin;
	}

	private IRssChannel getRssChannel(String id) throws IOException {
		IRssChannel rssChannel = rssChannels.get(id);
		if (rssChannel == null) {
			rssChannel = networkClient.getRssChannel(token, id);
			rssChannels.put(id, rssChannel);
		}

		return rssChannel;
	}

	private IRssItem getRssItem(String id) throws IOException {
		IRssItem rssItem = rssItems.get(id);
		if (rssItem == null) {
			rssItem = networkClient.getRssItem(token, id);
			rssItems.put(id, rssItem);
		}

		return rssItem;
	}

	private IDashboardLayout getDashboardLayout() throws IOException {
		return networkClient.getDashboardLayout(token);
	}

	private IDashboardMapping getDashboardMapping() throws IOException {
		LocalDate fromLocalDate = fromDatePicker.getValue();
		LocalDate tillLocalDate = tillDatePicker.getValue();
		return networkClient.getDashboardMapping(
				token,
				fromLocalDate != null
						? fromLocalDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
						: 0,
				tillLocalDate != null
						? tillLocalDate.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()
						: new Date().getTime(),
				Arrays.asList(keywordsTextField.getText().split(" ")));
	}

	private void disableInputs(boolean disable) {
		Platform.runLater(() -> {
			rootPane.setDisable(disable);
		});
	}

	private void close(boolean promptLogin) {
		this.promptLogin = promptLogin;
		cacheTimer.cancel();
		Platform.runLater(() -> {
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
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

	private void redraw() {
		runTask(() -> {
			disableInputs(true);

			try {
				Thread.sleep(100);
				IDashboardLayout dashboardLayout = getDashboardLayout();
				IDashboardMapping dashboardMapping = getDashboardMapping();
			} catch (HttpResponseException e) {
				switch (e.getStatusCode()) {
				case HttpStatusCodes.STATUS_CODE_UNAUTHORIZED:
					Alerts.showErrorAlert("Invalid credentials!", "Please try signing in again.");
					break;
				default:
					Alerts.showServerUnavailableAlert();
					break;
				}

				close(true);
			} catch (IOException e) {
				Alerts.showServerUnavailableAlert();
				close(true);
			} catch (InterruptedException e) {

			}

			disableInputs(false);
		});
	}

	@FXML
	public void initialize() {
		setupCacheTimer();
		redraw();
	}

	@FXML
	public void handleKeywordsKeyPressed(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ENTER) {
			redraw();
		} else if (code == KeyCode.ESCAPE) {
			keywordsTextField.clear();
		}
	}

	private void runTask(Runnable runnable) {
		currentTask = currentTask.thenRunAsync(runnable);
	}
}

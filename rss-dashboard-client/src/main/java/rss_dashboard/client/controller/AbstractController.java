package rss_dashboard.client.controller;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class AbstractController {
	@FXML
	protected Pane rootPane;

	protected void close() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private CompletableFuture<?> taskQueue = CompletableFuture.completedFuture(0);

	private void disableInputs(boolean disable) {
		rootPane.setDisable(disable);
	}

	protected synchronized void queueTask(CompletableFuture<?> task) {
		taskQueue = taskQueue
				.thenRunAsync(() -> {
					disableInputs(true);
				}, Platform::runLater)
				.thenComposeAsync(any -> task)
				.thenAcceptAsync(any -> {
					disableInputs(false);
				}, Platform::runLater);
	}
}

package rss_dashboard.client.controller;

import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public abstract class AbstractController {
	@FXML
	protected Pane rootPane;

	private CompletableFuture<?> taskQueue = CompletableFuture.completedFuture(0);

	private void disableInputs(boolean disable) {
		rootPane.setDisable(disable);
	}

	protected synchronized void runTask(CompletableFuture<?> task) {
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

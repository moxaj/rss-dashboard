package rss_dashboard.client.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
	public static void showErrorAlert(String headerText, String contentText) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setResizable(false);
			alert.setHeaderText(headerText);
			alert.setContentText(contentText);
			alert.show();
		});
	}

	public static void showServerUnavailableAlert() {
		showErrorAlert(
				"Could not connect to the server!",
				"The server might be unavailable. Please try again at a later time.");
	}
}

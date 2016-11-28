package rss_dashboard.client.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {
	public synchronized static void showErrorAlert(String headerText, String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setResizable(false);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showServerUnavailableAlert() {
		showErrorAlert("Could not connect to the server!",
				"The server might be unavailable. Please try again at a later time.");
	}

	public synchronized static boolean showConfirmationAlert(String headerText, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setResizable(false);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		return alert.showAndWait().get() == ButtonType.OK;
	}
}

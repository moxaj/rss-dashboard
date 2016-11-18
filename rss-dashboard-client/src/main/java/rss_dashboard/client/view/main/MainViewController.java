package rss_dashboard.client.view.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainViewController {
	@FXML
	private TabPane rssTabPane;
	@FXML
	private DatePicker fromDatePicker;
	@FXML
	private DatePicker tillDatePicker;
	@FXML
	private TextField keywordsTextField;

	private String token = null;
	private boolean promptLogin = false;

	public void setToken(String token) {
		this.token = token;
	}

	public boolean doPromptLogin() {
		return promptLogin;
	}

	private void close(boolean promptLogin) {
		this.promptLogin = promptLogin;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage stage = (Stage) rssTabPane.getScene().getWindow();
				stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
			}
		});
	}

	@FXML
	public void initialize() {

	}
}

package rss_dashboard.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AddRssChannelController extends AbstractController {
	@FXML
	protected TextField urlTextField;
	@FXML
	protected Button addButton;

	private String url;

	public String getUrl() {
		return url;
	}

	@FXML
	public void initialize() {
		addButton.setDisable(true);
		urlTextField.textProperty().addListener(event -> {
			addButton.setDisable(urlTextField.getText().isEmpty());
		});
	}

	@FXML
	public void handleAddButtonPressed(ActionEvent event) {
		this.url = urlTextField.getText();
		close();
	}

	@FXML
	public void handleUrlTextFieldKeyPressed(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ESCAPE) {
			urlTextField.clear();
		} else if (code == KeyCode.ENTER) {
			this.url = urlTextField.getText();
			close();
		}
	}
}

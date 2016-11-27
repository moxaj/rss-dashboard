package rss_dashboard.client.controller;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpStatusCodes;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rss_dashboard.client.network.ILoginNetworkClient;

public class LoginController extends AbstractController {
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private Button loginButton;

	private ILoginNetworkClient networkClient;
	private String token;

	public void setNetworkClient(ILoginNetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	public String getToken() {
		return token;
	}

	@FXML
	protected void initialize() {
		loginButton.setDisable(true);

		ChangeListener<? super String> listener = (observable, oldValue, newValue) -> {
			updateLoginButton();
		};

		emailTextField.textProperty().addListener(listener);
		passwordTextField.textProperty().addListener(listener);
	}

	private boolean isLoginEnabled() {
		return !emailTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty();
	}

	private void updateLoginButton() {
		loginButton.setDisable(!isLoginEnabled());
	}

	private void close() {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}

	private void login() {
		runTask(networkClient
				.login(emailTextField.getText(), passwordTextField.getText())
				.exceptionally(ex -> {
					Throwable cause = ex.getCause();
					if (cause instanceof HttpResponseException) {
						if (((HttpResponseException) ex)
								.getStatusCode() == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED) {
							Alerts.showErrorAlert("Invalid credentials!",
									"Please verify your email / password combination.");
							return null;
						}
					}

					Alerts.showServerUnavailableAlert();
					return null;
				}).thenAcceptAsync(token -> {
					this.token = token;
					close();
				}, Platform::runLater));
	}

	@FXML
	protected void handleLoginButton() {
		login();
	}

	@FXML
	protected void handleKeyPressed(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ENTER && isLoginEnabled()) {
			login();
		} else if (code == KeyCode.ESCAPE) {
			close();
		}
	}
}

package rss_dashboard.client.view.login;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rss_dashboard.client.network.INetworkLoginClient;
import rss_dashboard.client.view.Alerts;

public class LoginViewController {
	@FXML
	private Pane rootPane;
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private Button loginButton;

	private final INetworkLoginClient networkClient;
	private String token;

	public LoginViewController(INetworkLoginClient networkClient) {
		this.networkClient = networkClient;
	}

	public String getToken() {
		return token;
	}

	@FXML
	protected void initialize() {
		updateLoginButton();

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

	private void disableInputs(boolean disable) {
		Platform.runLater(() -> {
			rootPane.setDisable(disable);
		});
	}

	private void close() {
		Platform.runLater(() -> {
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
	}

	private void login() {
		if (networkClient == null) {
			throw new RuntimeException("LoginViewController: INetworkLoginClient not set!");
		}

		CompletableFuture.runAsync(() -> {
			disableInputs(true);

			try {
				token = networkClient.login(emailTextField.getText(), passwordTextField.getText());
				close();
			} catch (HttpResponseException e) {
				switch (e.getStatusCode()) {
				case HttpStatusCodes.STATUS_CODE_UNAUTHORIZED:
					Alerts.showErrorAlert("Invalid credentials!", "Please verify your email / password combination.");
					break;
				default:
					Alerts.showServerUnavailableAlert();
					break;
				}
			} catch (IOException e) {
				Alerts.showServerUnavailableAlert();
			}

			disableInputs(false);
		});
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

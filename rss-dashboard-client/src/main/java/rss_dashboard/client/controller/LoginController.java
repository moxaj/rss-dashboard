package rss_dashboard.client.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import rss_dashboard.client.network.ILoginNetworkClient;

public class LoginController extends AbstractController {
	@FXML
	protected TextField emailTextField;
	@FXML
	protected Button authButton;

	private HostServices hostServices;
	private String clientId;
	private ILoginNetworkClient networkClient;
	private String tempToken;
	private String token;
	private boolean authorized;

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setNetworkClient(ILoginNetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	public void setTempToken(String tempTokenPath) {
		File file = new File(tempTokenPath);
		if (file.exists()) {
			try {
				tempToken = Files.readAllLines(Paths.get(tempTokenPath)).get(0);
				authorized = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			tempToken = UUID.randomUUID().toString();
			authorized = false;
			try {
				Files.write(Paths.get(tempTokenPath), Arrays.asList(new String[] { tempToken }));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		updateAuthButton();
	}

	public String getToken() {
		return token;
	}

	private void updateAuthButton() {
		authButton.setText(authorized ? "Login" : "Authorize");
	}

	@FXML
	public void handleAuthButtonPressed(ActionEvent event) {
		if (authorized) {
			queueTask(networkClient.login(emailTextField.getText(), tempToken)
					.thenAcceptAsync(token -> {
						this.token = token;
						close();
					}, Platform::runLater)
					.exceptionally(ex -> {
						Platform.runLater(() -> {
							Alerts.showServerUnavailableAlert();
							authorized = false;
							updateAuthButton();
						});

						return null;
					}));
		} else {
			String url = "https://accounts.google.com/o/oauth2/v2/auth"
					+ "?scope=email%20profile"
					+ "&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Frss-dashboard%2Fmisc%2Fgoogle%2Fregister"
					+ "&response_type=code"
					+ "&client_id=" + clientId
					+ "&state=" + tempToken
					+ "&login_hint=" + emailTextField.getText();
			hostServices.showDocument(url);
			authorized = true;
			updateAuthButton();
		}
	}
}

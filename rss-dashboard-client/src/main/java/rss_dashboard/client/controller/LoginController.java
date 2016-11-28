package rss_dashboard.client.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
	protected Button accessButton;
	@FXML
	protected Button connectButton;

	private HostServices hostServices;
	private String clientId;
	private ILoginNetworkClient networkClient;
	private String tempTokenPath;
	private String tempToken;
	private String token;

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setNetworkClient(ILoginNetworkClient networkClient) {
		this.networkClient = networkClient;
	}

	public void setTempTokenPath(String tempTokenPath) {
		this.tempTokenPath = tempTokenPath;

		File file = new File(tempTokenPath);
		if (file.exists()) {
			try {
				tempToken = Files.readAllLines(Paths.get(tempTokenPath)).get(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		updateButtons();
	}

	public String getToken() {
		return token;
	}

	private void updateButtons() {
		accessButton.setText(tempToken == null ? "Authorize" : "Unauthorize");
		connectButton.setDisable(tempToken == null);
	}

	@FXML
	public void handleAccessButtonPressed(ActionEvent event) {
		if (tempToken == null) {
			tempToken = UUID.randomUUID().toString();
			try {
				Files.write(Paths.get(tempTokenPath), tempToken.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}

			String url = "https://accounts.google.com/o/oauth2/v2/auth"
					+ "?scope=email%20profile"
					+ "&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Frss-dashboard%2Fmisc%2Fgoogle%2Fregister"
					+ "&response_type=code"
					+ "&client_id=" + clientId
					+ "&state=" + tempToken
					+ "&login_hint=" + emailTextField.getText();
			hostServices.showDocument(url);
			updateButtons();
		} else {
			new File(tempTokenPath).delete();
			tempToken = null;
			updateButtons();

			queueTask(networkClient.unauthorize(tempToken)
					.exceptionally(ex -> {
						Platform.runLater(() -> {
							Alerts.showServerUnavailableAlert();
							updateButtons();
						});

						return null;
					}));
		}
	}

	@FXML
	public void handleConnectButtonPressed(ActionEvent event) {
		this.token = tempToken;
		close();
	}
}

package rss_dashboard.client.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import rss_dashboard.client.network.ILoginNetworkClient;

public class LoginController extends AbstractController {
	@FXML
	protected TextField emailTextField;
	@FXML
	protected Button loginButton;

	private HostServices hostServices;
	private String clientId;
	private ILoginNetworkClient networkClient;
	private String tempTokenPath;
	private Properties tempTokenProperties;
	private String tempToken;

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

		tempTokenProperties = new Properties();
		try {
			tempTokenProperties.load(new FileInputStream(tempTokenPath));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file!", e);
		}
	}

	public String getToken() {
		return tempToken;
	}

	@FXML
	public void handleLoginButtonPressed(ActionEvent event) {
		String email = emailTextField.getText();
		String emailHash = Integer.toString(email.hashCode());
		tempToken = tempTokenProperties.getProperty(emailHash);
		if (tempToken == null) {
			tempToken = UUID.randomUUID().toString();
			tempTokenProperties.setProperty(emailHash, tempToken);

			try {
				tempTokenProperties.store(new PrintWriter(tempTokenPath), "");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			String url = "https://accounts.google.com/o/oauth2/v2/auth"
					+ "?scope=email%20profile"
					+ "&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Frss-dashboard%2Fmisc%2Fgoogle%2Fregister"
					+ "&response_type=code"
					+ "&client_id=" + clientId
					+ "&state=" + tempToken
					+ "&login_hint=" + emailTextField.getText();
			hostServices.showDocument(url);
		} else {
			close();
			// new File(tempTokenPath).delete();
			// tempToken = null;
			// updateButtons();
			//
			// queueTask(networkClient.unauthorize(tempToken)
			// .exceptionally(ex -> {
			// Platform.runLater(() -> {
			// Alerts.showServerUnavailableAlert();
			// updateButtons();
			// });
			//
			// return null;
			// }));
		}
	}
}

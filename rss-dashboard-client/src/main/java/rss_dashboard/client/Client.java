package rss_dashboard.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import rss_dashboard.client.controller.Alerts;
import rss_dashboard.client.controller.LoginController;
import rss_dashboard.client.controller.MainController;
import rss_dashboard.client.network.HttpClient;
import rss_dashboard.client.network.INetworkClient;

public class Client extends Application {
	private final Properties properties;
	private final INetworkClient networkClient;

	public Client() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/main/resources/network.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file!", e);
		}

		try {
			networkClient = new HttpClient(properties.getProperty("baseUrl"));
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed server url!", e);
		}
	}

	private void showLoginScene() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		LoginController controller = loader.getController();

		Stage loginStage = new Stage();
		loginStage.setTitle("Sign in with Google credentials");
		loginStage.setResizable(false);
		loginStage.initStyle(StageStyle.UTILITY);
		loginStage.setScene(new Scene(pane));
		loginStage.setOnCloseRequest(event -> {
			String token = controller.getToken();
			if (token != null) {
				showMainScene(token);
			} else {
				networkClient.shutdown();
			}
		});
		loginStage.show();
	}

	private void showMainScene(String token) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		MainController controller = loader.getController();
		controller.setNetworkClient(networkClient);
		controller.setHostServices(getHostServices());
		controller.setToken(token);
		controller.load();

		Stage mainStage = new Stage();
		mainStage.setScene(new Scene(pane));
		mainStage.setTitle("RSS Dashboard Reader");
		mainStage.centerOnScreen();
		mainStage.initStyle(StageStyle.UNIFIED);
		mainStage.setOnCloseRequest(event -> {
			if (controller.doPromptLogin()) {
				showLoginScene();
			} else {
				networkClient.shutdown();
			}
		});
		mainStage.show();
	}

	@Override
	public void start(Stage primaryStage) {
		showLoginScene();
	}

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

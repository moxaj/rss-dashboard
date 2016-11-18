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
import rss_dashboard.client.network.INetworkClient;
import rss_dashboard.client.network.NetworkClient;
import rss_dashboard.client.view.login.LoginViewController;
import rss_dashboard.client.view.main.MainViewController;

public class Main extends Application {
	public static final boolean DEBUG = true;

	private final Properties properties;
	private final INetworkClient networkClient;

	public Main() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/main/resources/network.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file!", e);
		}

		try {
			networkClient = new NetworkClient(properties.getProperty("baseUrl"));
		} catch (MalformedURLException e) {
			throw new RuntimeException("Malformed server url!", e);
		}
	}

	private void showLoginScene() {
		FXMLLoader loader = new FXMLLoader(LoginViewController.class.getResource("LoginView.fxml"));
		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		LoginViewController controller = loader.getController();
		controller.setNetworkLoginClient(networkClient);

		Stage loginStage = new Stage();
		loginStage.setTitle("Sign in with Google credentials");
		loginStage.setResizable(false);
		loginStage.initStyle(StageStyle.UTILITY);
		loginStage.setScene(new Scene(pane));
		loginStage.setOnCloseRequest(event -> {
			String token = controller.getToken();
			if (token != null || DEBUG) {
				showMainScene(token);
			}
		});
		loginStage.show();
	}

	private void showMainScene(String token) {
		FXMLLoader loader = new FXMLLoader(MainViewController.class.getResource("MainView.fxml"));
		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		MainViewController controller = loader.getController();
		controller.setToken(token);

		Stage mainStage = new Stage();
		mainStage.setScene(new Scene(pane));
		mainStage.setTitle("RSS Dashboard Reader");
		mainStage.setResizable(false);
		mainStage.centerOnScreen();
		mainStage.initStyle(StageStyle.UNIFIED);
		mainStage.setOnCloseRequest(event -> {
			if (controller.doPromptLogin()) {
				showLoginScene();
			}
		});
		mainStage.show();
	}

	@Override
	public void start(Stage primaryStage) {
		showLoginScene();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package rss_dashboard.client.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class LoginController {
	@FXML
	protected WebView webView;

	private String token;
	
	public String getToken() {
		return token;
	}
	
	@FXML
	public void initialize() {
		WebEngine webEngine = webView.getEngine();
		webEngine.load(LoginController.class.getResource("/html/Login.html").toString());

		webEngine.getLoadWorker().stateProperty().addListener(
				new ChangeListener<State>() {
					@Override
					public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
						if (newState == State.SUCCEEDED) {
							JSObject window = (JSObject) webEngine.executeScript("window");
							window.setMember("java", new JsBridge());
						}
					}
				});
	}

	public class JsBridge {
		public void setToken(String token) {
			LoginController.this.token = token;
			System.out.println("TOKEN: " + token);
		}
	}
}

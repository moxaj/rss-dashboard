package rss_dashboard.client.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmptyRssChannelController extends AbstractController {
	@FXML
	protected Button addButton;

	private int row;
	private int column;
	private String url;
	private Runnable addCallback;

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public String getUrl() {
		return url;
	}

	public void setAddCallback(Runnable addCallback) {
		this.addCallback = addCallback;
	}

	@FXML
	public void handleAddButtonPressed(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddRssChannelView.fxml"));

		Pane pane;
		try {
			pane = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		AddRssChannelController controller = loader.getController();

		Stage stage = new Stage();
		stage.setTitle("Add new RSS feed");
		stage.setResizable(false);
		stage.initStyle(StageStyle.UTILITY);
		stage.setScene(new Scene(pane));
		stage.setOnCloseRequest(closeEvent -> {
			this.url = controller.getUrl();
			if (url != null) {
				addCallback.run();
			}
		});
		stage.show();
	}
}

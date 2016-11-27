package rss_dashboard.client.controller;

import java.io.IOException;
import java.util.List;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssItem;

public class RssChannelController {
	@FXML
	protected Label titleLabel;
	@FXML
	protected Hyperlink hyperlink;
	@FXML
	protected Label descriptionLabel;
	@FXML
	protected ListView<Node> itemListView;
	@FXML
	protected Button deleteButton;

	private Runnable deleteCallback;
	private HostServices hostServices;

	public void setDeleteCallback(Runnable deleteCallback) {
		this.deleteCallback = deleteCallback;
	}

	public void setHostServices(HostServices hostServices) {
		this.hostServices = hostServices;
		hyperlink.setOnAction(event -> {
			hostServices.showDocument(hyperlink.getText());
		});
	}

	public void setRssChannel(IRssChannel rssChannel) {
		titleLabel.setText(rssChannel.getTitle());
		hyperlink.setText(rssChannel.getLink());
		descriptionLabel.setText(rssChannel.getDescription());
	}

	public void setRssItems(List<IRssItem> rssItems) {
		itemListView.getItems().clear();

		for (IRssItem rssItem : rssItems) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RssItemView.fxml"));

			Pane pane;
			try {
				pane = loader.load();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

			RssItemController controller = loader.getController();
			controller.setRssItem(rssItem);
			controller.setHostServices(hostServices);
			itemListView.getItems().add(pane);
		}

	}

	@FXML
	public void handleDeleteButtonPressed(ActionEvent event) {
		if (Alerts.showConfirmationAlert("Would you really like to remove this channel?", "")) {
			deleteCallback.run();
		}
	}
}

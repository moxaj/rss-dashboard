package rss_dashboard.client.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import rss_dashboard.common.model.rss.IRssItem;

public class RssItemController {
	@FXML
	protected Label titleLabel;
	@FXML
	protected Hyperlink hyperlink;
	@FXML
	protected Label descriptionLabel;
	@FXML
	protected Label pubDateLabel;

	public void setRssItem(IRssItem rssItem) {
		titleLabel.setText(rssItem.getTitle());
		hyperlink.setText(rssItem.getLink());
		descriptionLabel.setText(rssItem.getDescription());
		pubDateLabel.setText(rssItem.getPubDate());
	}

	public void setHostServices(HostServices hostServices) {
		hyperlink.setOnAction(event -> {
			hostServices.showDocument(hyperlink.getText());
		});
	}
}

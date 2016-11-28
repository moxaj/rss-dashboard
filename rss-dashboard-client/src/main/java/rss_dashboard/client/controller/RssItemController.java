package rss_dashboard.client.controller;

import java.nio.charset.StandardCharsets;

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

	private String url;

	public void setRssItem(IRssItem rssItem) {
		titleLabel.setText(rssItem.getTitle());
		hyperlink.setText("link");

		String description = rssItem.getDescription();
		new String(description.getBytes(), StandardCharsets.UTF_8);
		descriptionLabel.setText(description == null || description.isEmpty()
				? "No description given."
				: description);

		pubDateLabel.setText(rssItem.getPubDate());
		this.url = rssItem.getLink();
	}

	public void setHostServices(HostServices hostServices) {
		hyperlink.setOnAction(event -> {
			hostServices.showDocument(url);
		});
	}
}

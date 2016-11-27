package rss_dashboard.client.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
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
	protected Label pubDateLabel;
	@FXML
	protected ImageView image;
	@FXML
	protected ListView<Node> itemListView;

	public void setRssChannel(IRssChannel rssChannel) {
		titleLabel.setText(rssChannel.getTitle());
		hyperlink.setText(rssChannel.getLink());
		descriptionLabel.setText(rssChannel.getDescription());
		pubDateLabel.setText(rssChannel.getPubDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		// image.setImage(new Image(rssChannel.getImageUrl(), true));
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
			itemListView.getItems().add(pane);
		}
	}
}

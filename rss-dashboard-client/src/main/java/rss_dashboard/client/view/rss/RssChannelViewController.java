package rss_dashboard.client.view.rss;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class RssChannelViewController {
	@FXML
	protected Hyperlink titleLabel;
	@FXML
	protected Label descriptionLabel;
	@FXML
	protected Label categoriesLabel;
	@FXML
	protected Label pubDateLabel;
	@FXML
	protected ImageView image;
	@FXML
	protected ListView<RssItemViewController> itemListView;
}

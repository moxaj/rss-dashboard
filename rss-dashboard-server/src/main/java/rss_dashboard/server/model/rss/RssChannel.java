package rss_dashboard.server.model.rss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.rss.IRssChannel;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class RssChannel extends RssElement implements IRssChannel {
	private String language;
	private String imageUrl;

	@Builder
	private RssChannel(String id, String title, String link, String description, List<String> categories,
			LocalDate pubDate, String language, String imageUrl) {
		super(id, title, link, description, categories, pubDate);
		this.language = language;
		this.imageUrl = imageUrl;
	}
	
	public boolean loadDetails() {
		SyndFeed syndFeed = getFeed();
		
		List<SyndCategory> categories = syndFeed.getCategories();
		List<String> stringCategories = new ArrayList<>();

		for (SyndCategory category : categories) {
			stringCategories.add(category.getName());
		}

		setTitle(syndFeed.getTitle());
		setDescription(syndFeed.getDescription());
		setCategories(stringCategories);
		setPubDate(syndFeed.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		setLanguage(syndFeed.getLanguage());
		setImageUrl(syndFeed.getImage().getUrl());
		
		return true;
	}
	
	public SyndFeed getFeed() {
		SyndFeed syndFeed = null;
		InputStream inputStream = null;

		try {
			inputStream = new URL(getLink()).openConnection().getInputStream();
			syndFeed = new SyndFeedInput().build(new InputSource(inputStream));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return syndFeed;
	}
}

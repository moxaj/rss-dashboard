package rss_dashboard.server.network;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.derby.tools.sysinfo;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.model.rss.IRssChannelMapping;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.server.model.misc.AuthorizationException;
import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.rss.RssChannel;
import rss_dashboard.server.model.rss.RssChannelMapping;
import rss_dashboard.server.model.rss.RssItem;
import rss_dashboard.server.repository.RepositoryException;
import rss_dashboard.server.repository.RssChannelRepository;
import rss_dashboard.server.repository.RssItemRepository;

@Path("/rss")
public class RssHttpServlet extends AbstractHttpServlet {
	@GET
	@Path("/channels")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssChannel getRssChannel(@QueryParam("id") String id) {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		response.setCharacterEncoding("UTF-8");

		try {
			List<RssChannel> results = new RssChannelRepository().query(RssChannel.builder().id(id).build());

			if (results.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND_404);
				return null;
			} else {
				return results.get(0);
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}
	}

	@GET
	@Path("/mapping")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssChannelMapping getRssItemsOfChannel(@QueryParam("id") String id) {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		response.setCharacterEncoding("UTF-8");

		try {
			List<RssChannel> rssChannelResults = new RssChannelRepository().query(RssChannel.builder().id(id).build());

			if (rssChannelResults.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND_404);
				return null;
			}

			RssChannel rssChannel = rssChannelResults.get(0);

			SyndFeed syndFeed = rssChannel.getFeed();

			if (syndFeed == null) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
				return null;
			}

			List<SyndEntry> entries = syndFeed.getEntries();
			List<RssItem> rssItems = new ArrayList<>();

			int i = 0;

			for (SyndEntry entry : entries) {
				if (i > 30) {
					break;
				}

				List<SyndCategory> categories = entry.getCategories();
				Collections.reverse(categories);

				List<String> stringCategories = new ArrayList<>();

				for (SyndCategory category : categories) {
					stringCategories.add(category.getName());
				}

				rssItems.add(RssItem.builder().title(entry.getTitle()).link(entry.getLink())
						.description(entry.getDescription().getValue())
						.categories(stringCategories).pubDate(entry.getPublishedDate().toInstant()
								.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
						.author(entry.getAuthor()).build());

				i++;
			}

			RssItemRepository rssItemRepository = new RssItemRepository();

			List<String> ids = rssItemRepository.add(rssItems);

			return RssChannelMapping.builder().rssItemIds(ids).build();
		} catch (RepositoryException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}
	}

	@GET
	@Path("/items")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IRssItem getRssItem(@QueryParam("id") String id) {
		ClientProfile profile;

		try {
			profile = getClientProfile();
		} catch (AuthorizationException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}

		if (profile == null || profile.isExpired() || !profile.isValid()) {
			response.setStatus(HttpStatus.UNAUTHORIZED_401);
			return null;
		}

		response.setCharacterEncoding("UTF-8");

		try {
			List<RssItem> results = new RssItemRepository().query(RssItem.builder().id(id).build());

			if (results.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND_404);
				return null;
			} else {
				return results.get(0);
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			return null;
		}
	}
}

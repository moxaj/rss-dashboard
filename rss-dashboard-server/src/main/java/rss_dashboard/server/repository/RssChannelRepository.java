package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.rss.RssChannel;

public class RssChannelRepository extends AbstractRepository implements IRepository<RssChannel> {
	private String SQL_ADD = "INSERT INTO rss_channels "
			+ "(id, title, link, description, pubDate, language, imageUrl) "
			+ "VALUES ('?', '?', '?', '?', DATE('?'), '?', '?')";
	private String SQL_UPDATE = "UPDATE rss_channels " + "SET title = '?', " + "link = '?', " + "description = '?', "
			+ "pubDate = DATE('?'), " + "language = '?', " + "imageUrl = '?' " + "WHERE id = '?'";
	private String SQL_DELETE = "DELETE FROM rss_channels " + "WHERE id = '?'";
	private String SQL_SELECT = "SELECT * " + "FROM rss_channels " + "WHERE id = '?'";

	@Override
	public void add(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);

		add(helper);
	}

	@Override
	public void add(Iterable<RssChannel> items) throws RepositoryException {
		try {
			connect();

			for (RssChannel item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				String p2 = item.getTitle();
				String p3 = item.getLink();
				String p4 = item.getDescription();
				LocalDate p5 = item.getPubDate();
				String p6 = item.getLanguage();
				String p7 = item.getImageUrl();

				statement.setString(1, randomId());
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");
				statement.setString(4, p4 != null ? p4 : "");
				statement.setString(5, p5 != null ? p5.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
				statement.setString(6, p6 != null ? p6 : "");
				statement.setString(7, p7 != null ? p7 : "");

				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RepositoryException(e.getMessage());
		} finally {
			try {
				disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<RssChannel> items) throws RepositoryException {
		try {
			connect();

			for (RssChannel item : items) {
				PreparedStatement statement = prepareStatement(SQL_UPDATE);

				String p1 = item.getTitle();
				String p2 = item.getLink();
				String p3 = item.getDescription();
				LocalDate p4 = item.getPubDate();
				String p5 = item.getLanguage();
				String p6 = item.getImageUrl();
				String p7 = item.getId();

				if (p7 == null || p7.isEmpty()) {
					throw new RepositoryException("Id is null or empty.");
				}

				statement.setString(1, p1 != null ? p1 : "");
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");
				statement.setString(4, p4 != null ? p4.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");
				statement.setString(5, p5 != null ? p5 : "");
				statement.setString(6, p6 != null ? p6 : "");
				statement.setString(7, p7);

				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RepositoryException(e.getMessage());
		} finally {
			try {
				disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void remove(String id) throws RepositoryException {
		List<String> helper = new ArrayList<>();
		helper.add(id);

		remove(helper);
	}

	@Override
	public void remove(Iterable<String> ids) throws RepositoryException {
		try {
			connect();

			for (String id : ids) {
				PreparedStatement statement = prepareStatement(SQL_DELETE);

				statement.setString(1, id);

				statement.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RepositoryException(e.getMessage());
		} finally {
			try {
				disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<RssChannel> query(RssChannel filter) throws RepositoryException {
		try {
			connect();

			PreparedStatement statement = prepareStatement(SQL_SELECT);

			String p1 = filter.getId();

			statement.setString(1, p1 != null ? p1 : "");

			ResultSet results = statement.executeQuery();

			List<RssChannel> typedResults = new ArrayList<>();

			while (results.next()) {
				RssChannel rssChannel = RssChannel.builder().id(results.getString(1)).title(results.getString(2))
						.link(results.getString(3)).description(results.getString(4))
						.pubDate(results.getTimestamp(5).toLocalDateTime().toLocalDate()).language(results.getString(6))
						.imageUrl(results.getString(7)).build();

				typedResults.add(rssChannel);
			}

			return typedResults;
		} catch (SQLException e) {
			throw new RepositoryException(e.getMessage());
		} finally {
			try {
				disconnect();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

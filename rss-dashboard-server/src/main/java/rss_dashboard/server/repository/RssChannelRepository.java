package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rss_dashboard.server.model.rss.RssChannel;

public class RssChannelRepository extends AbstractRepository implements IRepository<RssChannel> {
	private String SQL_ADD = "INSERT INTO rss_channels " + "(id, link) " + "VALUES (?, ?)";
	private String SQL_UPDATE = "UPDATE rss_channels " + "SET link = ? " + "WHERE id = ?";
	private String SQL_DELETE = "DELETE FROM rss_channels " + "WHERE id = ?";
	private String SQL_SELECT = "SELECT * " + "FROM rss_channels " + "WHERE id = ?";

	private static Map<String, RssChannel> memoryRepository = new HashMap<>();

	@Override
	public String add(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);

		return add(helper).get(0);
	}

	@Override
	public List<String> add(Iterable<RssChannel> items) throws RepositoryException {
		try {
			connect();

			List<String> returnIds = new ArrayList<>();

			for (RssChannel item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				String p1 = randomId();
				String p2 = item.getLink();

				statement.setString(1, p1);
				statement.setString(2, p2 != null ? p2 : "");

				statement.executeUpdate();

				memoryRepository.put(p1, item);

				returnIds.add(p1);
			}

			return returnIds;
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

				String p1 = item.getLink();
				String p2 = item.getId();

				if (p2 == null || p2.isEmpty()) {
					throw new RepositoryException("Id is null or empty.");
				}

				statement.setString(1, p1 != null ? p1 : "");
				statement.setString(2, p2);

				statement.executeUpdate();

				memoryRepository.put(p2, item);
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

				memoryRepository.remove(id);
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
				RssChannel rssChannel = RssChannel.builder().id(results.getString(1)).link(results.getString(2)).build();
				
				if (memoryRepository.containsKey(rssChannel.getId())) {
					typedResults.add(memoryRepository.get(rssChannel.getId()));
				} else {
					if (rssChannel.loadDetails()) {
						memoryRepository.put(rssChannel.getId(), rssChannel);
						
						typedResults.add(rssChannel);
					}
				}
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

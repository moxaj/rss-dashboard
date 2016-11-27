package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.rss.RssCategory;

public class RssCategoryRepository extends AbstractRepository implements IRepository<RssCategory> {
	private String SQL_ADD = "INSERT INTO rss_categories " + "(id, category, rss_element_id) "
			+ "VALUES (?, ?, ?)";
	private String SQL_UPDATE = "UPDATE rss_categories " + "SET category = ?, " + "rss_element_id = ? "
			+ "WHERE id = ?";
	private String SQL_DELETE = "DELETE FROM rss_categories " + "WHERE id = ?";
	private String SQL_SELECT = "SELECT * " + "FROM rss_categories " + "WHERE id = ? OR " + "category = ? OR "
			+ "rss_element_id = ?";

	@Override
	public void add(RssCategory item) throws RepositoryException {
		List<RssCategory> helper = new ArrayList<>();
		helper.add(item);

		add(helper);
	}

	@Override
	public void add(Iterable<RssCategory> items) throws RepositoryException {
		try {
			connect();

			for (RssCategory item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				String p2 = item.getCategory();
				String p3 = item.getRssElementId();

				statement.setString(1, randomId());
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");

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
	public void update(RssCategory item) throws RepositoryException {
		List<RssCategory> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<RssCategory> items) throws RepositoryException {
		try {
			connect();

			for (RssCategory item : items) {
				PreparedStatement statement = prepareStatement(SQL_UPDATE);

				String p1 = item.getCategory();
				String p2 = item.getRssElementId();
				String p3 = item.getId();

				if (p3 == null || p3.isEmpty()) {
					throw new RepositoryException("Id is null or empty.");
				}

				statement.setString(1, p1 != null ? p1 : "");
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3);

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
	public List<RssCategory> query(RssCategory filter) throws RepositoryException {
		try {
			connect();

			PreparedStatement statement = prepareStatement(SQL_SELECT);

			String p1 = filter.getId();
			String p2 = filter.getCategory();
			String p3 = filter.getRssElementId();

			statement.setString(1, p1 != null ? p1 : "");
			statement.setString(2, p2 != null ? p2 : "");
			statement.setString(3, p3 != null ? p3 : "");

			ResultSet results = statement.executeQuery();

			List<RssCategory> typedResults = new ArrayList<>();

			while (results.next()) {
				RssCategory rssCategory = RssCategory.builder().id(results.getString(1)).category(results.getString(2))
						.rssElementId(results.getString(3)).build();

				typedResults.add(rssCategory);
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

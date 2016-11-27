package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.dashboard.DashboardElement;

public class DashboardElementRepository extends AbstractRepository implements IRepository<DashboardElement> {
	private String SQL_ADD = "INSERT INTO dashboard_elements " + "(id, x, y, w, h, page, client_id, rss_channel_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private String SQL_UPDATE = "UPDATE dashboard_elements " + "SET x = ?, " + "y = ?, " + "w = ?, " + "h = ?, "
			+ "page = ?, " + "client_id = ?, " + "rss_channel_id = ? " + "WHERE id = ?";
	private String SQL_DELETE = "DELETE FROM dashboard_elements " + "WHERE id = ?";
	private String SQL_SELECT = "SELECT * " + "FROM dashboard_elements " + "WHERE id = ? OR " + "client_id = ? OR "
			+ "rss_channel_id = ?";

	@Override
	public void add(DashboardElement item) throws RepositoryException {
		List<DashboardElement> helper = new ArrayList<>();
		helper.add(item);

		add(helper);
	}

	@Override
	public void add(Iterable<DashboardElement> items) throws RepositoryException {
		try {
			connect();

			for (DashboardElement item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				int p2 = item.getX();
				int p3 = item.getY();
				int p4 = item.getW();
				int p5 = item.getH();
				int p6 = item.getPage();
				String p7 = item.getClientId();
				String p8 = item.getChannelId();

				statement.setString(1, randomId());
				statement.setInt(2, p2);
				statement.setInt(3, p3);
				statement.setInt(4, p4);
				statement.setInt(5, p5);
				statement.setInt(6, p6);
				statement.setString(7, p7 != null ? p7 : "");
				statement.setString(8, p8 != null ? p8 : "");

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
	public void update(DashboardElement item) throws RepositoryException {
		List<DashboardElement> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<DashboardElement> items) throws RepositoryException {
		try {
			connect();

			for (DashboardElement item : items) {
				PreparedStatement statement = prepareStatement(SQL_UPDATE);

				int p1 = item.getX();
				int p2 = item.getY();
				int p3 = item.getW();
				int p4 = item.getH();
				int p5 = item.getPage();
				String p6 = item.getClientId();
				String p7 = item.getChannelId();
				String p8 = item.getId();

				if (p8 == null || p8.isEmpty()) {
					throw new RepositoryException("Id is null or empty.");
				}

				statement.setInt(1, p1);
				statement.setInt(2, p2);
				statement.setInt(3, p3);
				statement.setInt(4, p4);
				statement.setInt(5, p5);
				statement.setString(6, p6 != null ? p6 : "");
				statement.setString(7, p7 != null ? p7 : "");
				statement.setString(8, p8);

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
	public List<DashboardElement> query(DashboardElement filter) throws RepositoryException {
		try {
			connect();

			PreparedStatement statement = prepareStatement(SQL_SELECT);

			String p1 = filter.getId();
			String p2 = filter.getClientId();
			String p3 = filter.getChannelId();

			statement.setString(1, p1 != null ? p1 : "");
			statement.setString(2, p2 != null ? p2 : "");
			statement.setString(3, p3 != null ? p3 : "");

			ResultSet results = statement.executeQuery();

			List<DashboardElement> typedResults = new ArrayList<>();

			while (results.next()) {
				DashboardElement dashboardElement = DashboardElement.builder().id(results.getString(1))
						.x(results.getInt(2)).y(results.getInt(3)).w(results.getInt(4)).h(results.getInt(5))
						.page(results.getInt(6)).clientId(results.getString(7)).channelId(results.getString(8)).build();

				typedResults.add(dashboardElement);
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

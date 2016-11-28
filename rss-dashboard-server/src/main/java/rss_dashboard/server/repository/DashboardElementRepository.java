package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.dashboard.DashboardElement;

public class DashboardElementRepository extends AbstractRepository implements IRepository<DashboardElement> {
	private String SQL_ADD = "INSERT INTO dashboard_elements " + "(id, row, col, page, client_id, rss_channel_id) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";
	private String SQL_UPDATE = "UPDATE dashboard_elements " + "SET row = ?, " + "col = ?, " + "page = ?, "
			+ "client_id = ?, " + "rss_channel_id = ? " + "WHERE id = ?";
	private String SQL_DELETE = "DELETE FROM dashboard_elements " + "WHERE id = ?";
	private String SQL_SELECT = "SELECT * " + "FROM dashboard_elements " + "WHERE id = ? OR " + "client_id = ? OR "
			+ "rss_channel_id = ? ORDER BY page ASC";

	@Override
	public String add(DashboardElement item) throws RepositoryException {
		List<DashboardElement> helper = new ArrayList<>();
		helper.add(item);

		return add(helper).get(0);
	}

	@Override
	public List<String> add(Iterable<DashboardElement> items) throws RepositoryException {
		try {
			connect();
			
			List<String> returnIds = new ArrayList<>();

			for (DashboardElement item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				String p1 = randomId();
				int p2 = item.getRow();
				int p3 = item.getColumn();
				int p4 = item.getPage();
				String p5 = item.getClientId();
				String p6 = item.getChannelId();

				statement.setString(1, p1);
				statement.setInt(2, p2);
				statement.setInt(3, p3);
				statement.setInt(4, p4);
				statement.setString(5, p5 != null ? p5 : "");
				statement.setString(6, p6 != null ? p6 : "");

				statement.executeUpdate();
				
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

				int p1 = item.getRow();
				int p2 = item.getColumn();
				int p3 = item.getPage();
				String p4 = item.getClientId();
				String p5 = item.getChannelId();
				String p6 = item.getId();

				if (p6 == null || p6.isEmpty()) {
					throw new RepositoryException("Id is null or empty.");
				}

				statement.setInt(1, p1);
				statement.setInt(2, p2);
				statement.setInt(3, p3);
				statement.setString(4, p4 != null ? p4 : "");
				statement.setString(5, p5 != null ? p5 : "");
				statement.setString(6, p6);

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

			statement.setString(1, p1 != null ? p1 : "-");
			statement.setString(2, p2 != null ? p2 : "-");
			statement.setString(3, p3 != null ? p3 : "-");

			ResultSet results = statement.executeQuery();

			List<DashboardElement> typedResults = new ArrayList<>();

			while (results.next()) {
				DashboardElement dashboardElement = DashboardElement.builder().id(results.getString(1))
						.row(results.getInt(2)).column(results.getInt(3)).page(results.getInt(4))
						.clientId(results.getString(5)).channelId(results.getString(6)).build();

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

package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.misc.AuthorizationProviders;
import rss_dashboard.server.model.misc.ClientProfile;

public class ClientProfileRepository extends AbstractRepository implements IRepository<ClientProfile> {
	private String SQL_ADD = "INSERT INTO clients " + "(id, email, token1, expiration, token2, provider) "
			+ "VALUES ('?', '?', '?', '?', '?', '?')";
	private String SQL_UPDATE = "UPDATE clients " + "SET email = '?', " + "token1 = '?', "
			+ "expiration = TIMESTAMP('?'), " + "token2 = '?' " + "provider = '?' " + "WHERE id = '?'";
	private String SQL_DELETE = "DELETE FROM clients " + "WHERE id = '?'";
	private String SQL_SELECT = "SELECT * " + "FROM clients " + "WHERE id = '?' OR " + "email = '?' OR "
			+ "token1 = '?' OR " + "token2 = '?'";

	@Override
	public void add(ClientProfile item) throws RepositoryException {
		List<ClientProfile> helper = new ArrayList<>();
		helper.add(item);

		add(helper);
	}

	@Override
	public void add(Iterable<ClientProfile> items) throws RepositoryException {
		try {
			connect();

			for (ClientProfile item : items) {
				PreparedStatement statement = prepareStatement(SQL_ADD);

				String p2 = item.getEmail();
				String p3 = item.getToken1();
				LocalDateTime p4 = item.getExpiration();
				String p5 = item.getToken2();
				AuthorizationProviders p6 = item.getProvider();

				statement.setString(1, randomId());
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");
				statement.setString(4, p4 != null ? p4.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
				statement.setString(5, p5 != null ? p5 : "");
				statement.setString(6, p6 != null ? p6.getText() : "");

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
	public void update(ClientProfile item) throws RepositoryException {
		List<ClientProfile> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<ClientProfile> items) throws RepositoryException {
		try {
			connect();

			for (ClientProfile item : items) {
				PreparedStatement statement = prepareStatement(SQL_UPDATE);

				String p1 = item.getEmail();
				String p2 = item.getToken1();
				LocalDateTime p3 = item.getExpiration();
				String p4 = item.getToken2();
				AuthorizationProviders p5 = item.getProvider();
				String p6 = item.getId();

				if (p6 == null) {
					throw new RepositoryException("Id is null.");
				}

				statement.setString(1, p1 != null ? p1 : "");
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
				statement.setString(4, p4 != null ? p4 : "");
				statement.setString(5, p5 != null ? p5.getText() : "");
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
	public List<ClientProfile> query(ClientProfile filter) throws RepositoryException {
		try {
			connect();

			PreparedStatement statement = prepareStatement(SQL_SELECT);

			String p1 = filter.getId();
			String p2 = filter.getEmail();
			String p3 = filter.getToken1();
			String p4 = filter.getToken2();

			statement.setString(1, p1 != null ? p1 : "");
			statement.setString(2, p2 != null ? p2 : "");
			statement.setString(3, p3 != null ? p3 : "");
			statement.setString(4, p4 != null ? p4 : "");

			ResultSet results = statement.executeQuery();

			List<ClientProfile> typedResults = new ArrayList<>();

			while (results.next()) {
				ClientProfile clientProfile = ClientProfile.builder().id(results.getString(1))
						.email(results.getString(2)).token1(results.getString(3))
						.expiration(results.getTimestamp(4).toLocalDateTime()).token2(results.getString(5))
						.provider(AuthorizationProviders.fromText(results.getString(6))).build();

				typedResults.add(clientProfile);
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

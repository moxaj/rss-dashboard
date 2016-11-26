package rss_dashboard.server.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.misc.ClientProfile;

public class ClientProfileRepository extends AbstractRepository implements IRepository<ClientProfile> {
	private String SQL_ADD    = "INSERT INTO clients " + 
									"(id, email, token1, token2) " + 
									"VALUES (?, ?, ?, ?)";
	private String SQL_UPDATE = "UPDATE clients " + 
									"SET email = ?, " + 
											"token1 = ?, " +
											"token2 = ? " + 
									"WHERE id = ?";
	private String SQL_DELETE = "DELETE FROM clients " + 
									"WHERE id = ?";
	private String SQL_SELECT = "SELECT * " + 
									"FROM clients " + 
									"WHERE id = ? OR " + 
											"email = ? OR " +
											"token1 = ? OR " + 
											"token2 = ?";

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
				String p4 = item.getToken2();
				
				statement.setString(1, randomId());
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");
				statement.setString(4, p4 != null ? p4 : "");
				
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
				String p3 = item.getToken2();
				String p4 = item.getId();
				
				if (p4 == null) {
					throw new RepositoryException("is_null_error");
				}
				
				statement.setString(1, p1 != null ? p1 : "");
				statement.setString(2, p2 != null ? p2 : "");
				statement.setString(3, p3 != null ? p3 : "");
				statement.setString(4, p4);
				
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
	public void remove(ClientProfile item) throws RepositoryException {
		List<ClientProfile> helper = new ArrayList<>();
		helper.add(item);

		remove(helper);
	}

	@Override
	public void remove(Iterable<ClientProfile> items) throws RepositoryException {
		try {
			connect();
			
			for (ClientProfile item : items) {
				PreparedStatement statement = prepareStatement(SQL_DELETE);
				
				String p1 = item.getId();
				
				if (p1 == null) {
					throw new RepositoryException("is_null_error");
				}
				
				statement.setString(1, p1);
				
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
				ClientProfile clientProfile = ClientProfile.builder()
												.id(results.getString(1))
												.email(results.getString(2))
												.token1(results.getString(3))
												.token2(results.getString(4)).build();
				
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

package rss_dashboard.server.repository;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;

public abstract class AbstractRepository {
	private Connection connection = null;

	public void connect() throws SQLException {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream("src/main/resources/network.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file!", e);
		}

		if (connection == null) {
			connection = DriverManager.getConnection(properties.getProperty("dbUrl"));
		}
	}

	public void transaction() throws SQLException {
		if (connection != null) {
			connection.setAutoCommit(false);
		}
	}

	public void commit() throws SQLException {
		if (connection != null) {
			connection.commit();
			connection.setAutoCommit(true);
		}
	}

	public void rollback() throws SQLException {
		if (connection != null) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
	
	public String randomId() {
		return UUID.randomUUID().toString();
	}

	public void disconnect() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}
	}
}

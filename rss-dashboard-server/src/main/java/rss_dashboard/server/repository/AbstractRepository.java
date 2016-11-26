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

	protected void connect() throws SQLException {
		Properties properties = new Properties();

		try {
			properties.load(new FileInputStream("src/main/resources/network.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file.", e);
		}

		if (connection == null) {
			connection = DriverManager.getConnection(properties.getProperty("dbUrl"));
		}
	}

	protected void transaction() throws SQLException {
		if (connection != null) {
			connection.setAutoCommit(false);
		}
	}

	protected void commit() throws SQLException {
		if (connection != null) {
			connection.commit();
			connection.setAutoCommit(true);
		}
	}

	protected void rollback() throws SQLException {
		if (connection != null) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
	}

	protected PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

	protected String randomId() {
		return UUID.randomUUID().toString();
	}

	protected void disconnect() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			connection = null;
		}
	}
}

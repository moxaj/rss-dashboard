package rss_dashboard.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import rss_dashboard.server.model.misc.ClientProfile;
import rss_dashboard.server.model.rss.RssChannel;
import rss_dashboard.server.repository.ClientProfileRepository;
import rss_dashboard.server.repository.RepositoryException;
import rss_dashboard.server.repository.RssChannelRepository;

public class Server {
	private final Properties properties;
	private final HttpServer httpServer;

	public Server() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("src/main/resources/network.properties"));
		} catch (Exception e) {
			throw new RuntimeException("Could not load properties file.", e);
		}

		httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(properties.getProperty("baseUrl")),
				new ResourceConfig().packages("rss_dashboard.server.network"), false);
	}

	public void start() throws IOException {
		httpServer.start();
		System.out.println(String.format("Http server started. WADL available at %s/application.wadl.",
				properties.getProperty("baseUrl")));
	}

	public void stop() {
		httpServer.shutdown();
		System.out.println("Http server stopped.");
	}

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
		System.in.read();
		server.stop();
	}
}

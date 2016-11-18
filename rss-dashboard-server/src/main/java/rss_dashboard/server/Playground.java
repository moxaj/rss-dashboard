package rss_dashboard.server;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Playground {
	// Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/rss-dashboard/";

    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in rss_dashboard.server.controller
        ResourceConfig resourceConfig = new ResourceConfig().packages("rss_dashboard.server.controller");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();
        System.out.println(String.format("Server application started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        
        try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        server.stop();
    }
}

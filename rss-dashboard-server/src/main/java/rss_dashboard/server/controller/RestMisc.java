package rss_dashboard.server.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rss_dashboard.server.network.misc.KeepAliveResponse;
import rss_dashboard.server.network.misc.LoginRequest;
import rss_dashboard.server.network.misc.LoginResponse;
import rss_dashboard.server.network.misc.LogoutResponse;

@Path("/misc")
public class RestMisc {

	@POST
	@Path("/login")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public LoginResponse login(LoginRequest request) {
		System.out.println("/misc/login");
		
		return LoginResponse.builder()
				.token("thisisdummytokenonly")
				.build();
	}

	@POST
	@Path("/logout")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public LogoutResponse logout(LoginRequest request) {
		System.out.println("/misc/logout");
		
		return LogoutResponse.builder()
				.build();
	}

	@POST
	@Path("/keepalive")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public KeepAliveResponse keepalive(LoginRequest request) {
		System.out.println("/misc/keepalive");
		
		return KeepAliveResponse.builder()
				.alive(true)
				.build();
	}

}

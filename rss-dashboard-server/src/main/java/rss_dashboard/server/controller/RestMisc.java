package rss_dashboard.server.controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import rss_dashboard.common.network.misc.IKeepAliveResponse;
import rss_dashboard.common.network.misc.ILoginRequest;
import rss_dashboard.common.network.misc.ILoginResponse;
import rss_dashboard.common.network.misc.ILogoutResponse;
import rss_dashboard.server.network.misc.KeepAliveResponse;
import rss_dashboard.server.network.misc.LoginResponse;
import rss_dashboard.server.network.misc.LogoutResponse;

@Path("/misc")
public class RestMisc {

	@POST
	@Path("/login")
	public ILoginResponse login(ILoginRequest request) {
		System.out.println("/misc/login");
		
		return LoginResponse.builder()
				.token("thisisdummytokenonly")
				.build();
	}

	@POST
	@Path("/logout")
	public ILogoutResponse logout(ILoginRequest request) {
		System.out.println("/misc/logout");
		
		return LogoutResponse.builder()
				.build();
	}

	@POST
	@Path("/keepalive")
	public IKeepAliveResponse keepalive(ILoginRequest request) {
		System.out.println("/misc/keepalive");
		
		return KeepAliveResponse.builder()
				.alive(true)
				.build();
	}

}

package rss_dashboard.server.controller;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import rss_dashboard.common.network.dashboard.IDashboardLayoutRequest;
import rss_dashboard.common.network.dashboard.IDashboardLayoutResponse;
import rss_dashboard.common.network.dashboard.IDashboardModificationRequest;
import rss_dashboard.common.network.dashboard.IDashboardModificationResponse;
import rss_dashboard.common.network.dashboard.IDashboardRequest;
import rss_dashboard.common.network.dashboard.IDashboardResponse;
import rss_dashboard.server.network.dashboard.DashboardLayoutResponse;
import rss_dashboard.server.network.dashboard.DashboardModificationResponse;
import rss_dashboard.server.network.dashboard.DashboardResponse;

@Path("/dashboard")
public class RestDashboard {

	@GET
	@Path("/layout")
	public IDashboardLayoutResponse layout(IDashboardLayoutRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardLayoutResponse.builder()
				.build();
	}
	
	@POST
	@Path("/layout")
	public IDashboardModificationResponse layoutModification1(IDashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}
	
	@DELETE
	@Path("/layout")
	public IDashboardModificationResponse layoutModification2(IDashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}

	@GET
	@Path("/dashboard")
	public IDashboardResponse dashboard(IDashboardRequest request) {
		System.out.println("/dashboard/dashboard");
		
		return DashboardResponse.builder()
				.build();
	}
	
}

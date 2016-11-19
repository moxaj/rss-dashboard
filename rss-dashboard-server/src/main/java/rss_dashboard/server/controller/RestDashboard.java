package rss_dashboard.server.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardLayoutResponse layout(IDashboardLayoutRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardLayoutResponse.builder()
				.build();
	}
	
	@POST
	@Path("/layout")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardModificationResponse layoutModification1(IDashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}
	
	@DELETE
	@Path("/layout")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardModificationResponse layoutModification2(IDashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}

	@GET
	@Path("/dashboard")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public IDashboardResponse dashboard(IDashboardRequest request) {
		System.out.println("/dashboard/dashboard");
		
		return DashboardResponse.builder()
				.build();
	}
	
}

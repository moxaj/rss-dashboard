package rss_dashboard.server.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rss_dashboard.server.network.dashboard.DashboardLayoutRequest;
import rss_dashboard.server.network.dashboard.DashboardLayoutResponse;
import rss_dashboard.server.network.dashboard.DashboardModificationRequest;
import rss_dashboard.server.network.dashboard.DashboardModificationResponse;
import rss_dashboard.server.network.dashboard.DashboardRequest;
import rss_dashboard.server.network.dashboard.DashboardResponse;

@Path("/dashboard")
public class RestDashboard {

	@GET
	@Path("/layout")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public DashboardLayoutResponse layout(DashboardLayoutRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardLayoutResponse.builder()
				.build();
	}
	
	@POST
	@Path("/layout")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public DashboardModificationResponse layoutModification1(DashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}
	
	@DELETE
	@Path("/layout")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public DashboardModificationResponse layoutModification2(DashboardModificationRequest request) {
		System.out.println("/dashboard/layout");
		
		return DashboardModificationResponse.builder()
				.build();
	}

	@GET
	@Path("/dashboard")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public DashboardResponse dashboard(DashboardRequest request) {
		System.out.println("/dashboard/dashboard");
		
		return DashboardResponse.builder()
				.build();
	}
	
}

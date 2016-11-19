package rss_dashboard.server.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rss_dashboard.server.network.rss.RssChannelRequest;
import rss_dashboard.server.network.rss.RssChannelResponse;
import rss_dashboard.server.network.rss.RssItemRequest;
import rss_dashboard.server.network.rss.RssItemResponse;

@Path("/rss")
public class RestRss {
	
	@GET
	@Path("/channels")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public RssChannelResponse channels(RssChannelRequest request) {
		System.out.println("/rss/channels");

		return RssChannelResponse.builder()
				.build();
	}
	
	@GET
	@Path("/items")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public RssItemResponse channels(RssItemRequest request) {
		System.out.println("/rss/items");
		
		return RssItemResponse.builder()
				.build();
	}

}

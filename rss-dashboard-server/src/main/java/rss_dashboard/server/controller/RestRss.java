package rss_dashboard.server.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import rss_dashboard.common.network.rss.IRssChannelRequest;
import rss_dashboard.common.network.rss.IRssChannelResponse;
import rss_dashboard.common.network.rss.IRssItemRequest;
import rss_dashboard.common.network.rss.IRssItemResponse;
import rss_dashboard.server.network.rss.RssChannelResponse;
import rss_dashboard.server.network.rss.RssItemResponse;

@Path("/rss")
public class RestRss {
	
	@GET
	@Path("/channels")
	public IRssChannelResponse channels(IRssChannelRequest request) {
		System.out.println("/rss/channels");

		return RssChannelResponse.builder()
				.build();
	}
	
	@GET
	@Path("/items")
	public IRssItemResponse channels(IRssItemRequest request) {
		System.out.println("/rss/items");
		
		return RssItemResponse.builder()
				.build();
	}

}

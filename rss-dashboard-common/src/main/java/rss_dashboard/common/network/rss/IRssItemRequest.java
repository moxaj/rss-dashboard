package rss_dashboard.common.network.rss;

import java.util.List;

import rss_dashboard.common.network.IAuthenticatedRequest;

public interface IRssItemRequest extends IAuthenticatedRequest {
	List<String> getIds();
}

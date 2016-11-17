package rss_dashboard.client.network.dashboard;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.client.network.AuthenticatedRequest;
import rss_dashboard.common.network.dashboard.IDashboardModificationRequest;

@Getter
public class DashboardModificationRequest extends AuthenticatedRequest implements IDashboardModificationRequest {
	@Key
	private final int pageId;
	@Key
	private final int rowId;
	@Key
	private final int columnId;
	@Key
	private final String feedUrl;

	@Builder
	private DashboardModificationRequest(String token, int pageId, int rowId, int columnId, String feedUrl) {
		super(token);
		this.pageId = pageId;
		this.rowId = rowId;
		this.columnId = columnId;
		this.feedUrl = feedUrl;
	}
}

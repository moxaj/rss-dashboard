package rss_dashboard.server.network.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.dashboard.IDashboardModificationRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class DashboardModificationRequest extends AuthenticatedRequest implements IDashboardModificationRequest {
	private final int pageId;
	private final int rowId;
	private final int columnId;
	private final String feedUrl;

	@Builder
	public DashboardModificationRequest(String token, int pageId, int rowId, int columnId, String feedUrl) {
		super(token);
		this.pageId = pageId;
		this.rowId = rowId;
		this.columnId = columnId;
		this.feedUrl = feedUrl;
	}
}

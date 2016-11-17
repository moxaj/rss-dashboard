package rss_dashboard.server.network.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardFilter;
import rss_dashboard.common.network.dashboard.IDashboardRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class DashboardRequest extends AuthenticatedRequest implements IDashboardRequest {
	private final IDashboardFilter filter;

	@Builder
	private DashboardRequest(String token, IDashboardFilter filter) {
		super(token);
		this.filter = filter;
	}
}

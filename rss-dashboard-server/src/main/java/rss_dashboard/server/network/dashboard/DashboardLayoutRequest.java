package rss_dashboard.server.network.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.dashboard.IDashboardLayoutRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class DashboardLayoutRequest extends AuthenticatedRequest implements IDashboardLayoutRequest {
	@Builder
	private DashboardLayoutRequest(String token) {
		super(token);
	}
}

package rss_dashboard.server.network.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardLayout;
import rss_dashboard.common.network.dashboard.IDashboardLayoutResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class DashboardLayoutResponse implements IDashboardLayoutResponse {
	private final IDashboardLayout layout;
}

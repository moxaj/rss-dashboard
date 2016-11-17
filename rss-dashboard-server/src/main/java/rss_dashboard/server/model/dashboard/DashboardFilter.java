package rss_dashboard.server.model.dashboard;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardFilter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class DashboardFilter implements IDashboardFilter {
	private final List<String> keywords;
	private final String fromPubDate;
	private final String toPubDate;
}

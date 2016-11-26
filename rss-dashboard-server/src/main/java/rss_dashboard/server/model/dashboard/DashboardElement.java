package rss_dashboard.server.model.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.dashboard.IDashboardElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardElement implements IDashboardElement {
	private String id;
	private int x;
	private int y;
	private int w;
	private int h;
	private int page;
	private String clientId;
	private String channelId;
}

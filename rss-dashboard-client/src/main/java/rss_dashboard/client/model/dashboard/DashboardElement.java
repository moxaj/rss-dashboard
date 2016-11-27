package rss_dashboard.client.model.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.api.client.util.Key;

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
	@Key
	private String id;
	@Key
	private int x;
	@Key
	private int y;
	@Key
	private int w;
	@Key
	private int h;
	@Key
	private int page;
	@Key
	private String clientId;
	@Key
	private String channelId;
}

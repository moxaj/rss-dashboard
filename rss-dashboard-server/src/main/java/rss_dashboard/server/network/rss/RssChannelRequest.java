package rss_dashboard.server.network.rss;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.rss.IRssItemRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class RssChannelRequest extends AuthenticatedRequest implements IRssItemRequest {
	private final List<String> ids;

	@Builder
	private RssChannelRequest(String token, List<String> ids) {
		super(token);
		this.ids = ids;
	}
}

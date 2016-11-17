package rss_dashboard.server.network.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.IKeepAliveRequest;
import rss_dashboard.server.network.AuthenticatedRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
public class KeepAliveRequest extends AuthenticatedRequest implements IKeepAliveRequest {
	@Builder
	private KeepAliveRequest(String token) {
		super(token);
	}
}

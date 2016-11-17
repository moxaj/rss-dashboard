package rss_dashboard.server.network.misc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.network.misc.IKeepAliveResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class KeepAliveResponse implements IKeepAliveResponse {
	private final boolean alive;
}

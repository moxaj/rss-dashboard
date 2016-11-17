package rss_dashboard.server.network.rss;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssChannel;
import rss_dashboard.common.network.rss.IRssChannelResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class RssChannelResponse implements IRssChannelResponse {
	private final Map<String, IRssChannel> channels;
}

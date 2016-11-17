package rss_dashboard.server.network.rss;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.rss.IRssItem;
import rss_dashboard.common.network.rss.IRssItemResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Getter
@Builder
public class RssItemResponse implements IRssItemResponse {
	private final Map<String, IRssItem> items;
}

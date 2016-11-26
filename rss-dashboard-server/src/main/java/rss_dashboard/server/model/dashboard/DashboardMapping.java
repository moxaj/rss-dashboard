package rss_dashboard.server.model.dashboard;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.dashboard.IDashboardMapping;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMapping implements IDashboardMapping {
	private Map<String, List<String>> mapping;
}

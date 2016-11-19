package rss_dashboard.server.model.dashboard;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.dashboard.IDashboardLayout;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardLayout implements IDashboardLayout {
	private List<String[][]> layout;
}

package rss_dashboard.client.model.dashboard;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rss_dashboard.common.model.dashboard.IDashboard;

@Getter
@Setter
@Builder
public class Dashboard implements IDashboard {
	private List<String[][]> layout;
}

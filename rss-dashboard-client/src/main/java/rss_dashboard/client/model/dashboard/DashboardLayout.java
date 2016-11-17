package rss_dashboard.client.model.dashboard;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardLayout;

@Getter
@Builder
public class DashboardLayout implements IDashboardLayout {
	@Key
	private final String[][][] rssChannelIds;
}

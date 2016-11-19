package rss_dashboard.client.model.dashboard;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.dashboard.IDashboardLayout;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardLayout implements IDashboardLayout {
	@Key
	private String[][][] rssChannelIds;
}

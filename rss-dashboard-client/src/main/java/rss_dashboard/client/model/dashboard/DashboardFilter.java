package rss_dashboard.client.model.dashboard;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboardFilter;

@Getter
@Builder
public class DashboardFilter implements IDashboardFilter {
	@Key
	private final List<String> keywords;
	@Key
	private final String fromPubDate;
	@Key
	private final String toPubDate;
}

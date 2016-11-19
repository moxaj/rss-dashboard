package rss_dashboard.client.model.dashboard;

import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.dashboard.IDashboardMapping;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMapping implements IDashboardMapping {
	@Key
	private Map<String, List<String>> mapping;
}

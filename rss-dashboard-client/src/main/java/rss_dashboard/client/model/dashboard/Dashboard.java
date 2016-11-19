package rss_dashboard.client.model.dashboard;

import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rss_dashboard.common.model.dashboard.IDashboard;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard implements IDashboard {
	@Key
	private Map<String, List<String>> rssChannelMapping;
}

package rss_dashboard.client.model.dashboard.impl;

import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

import lombok.Builder;
import lombok.Getter;
import rss_dashboard.common.model.dashboard.IDashboard;

@Getter
@Builder
public class Dashboard implements IDashboard {
	@Key
	private final Map<String, List<String>> ids;
}

package rss_dashboard.client.model.dashboard;

import java.util.List;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.dashboard.IDashboard;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard implements IDashboard {
	@Key
	private List<String[][]> layout;
}

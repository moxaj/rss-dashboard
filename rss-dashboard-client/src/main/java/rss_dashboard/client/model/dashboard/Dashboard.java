package rss_dashboard.client.model.dashboard;

import java.util.List;
import java.util.Map;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rss_dashboard.common.model.dashboard.IDashboard;
import rss_dashboard.common.model.dashboard.Position;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard implements IDashboard {
	@Key
	private int width;
	@Key
	private int height;
	@Key
	private List<Map<String, Position>> layout;
}

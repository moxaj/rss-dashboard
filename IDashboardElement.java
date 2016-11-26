package rss_dashboard.common.model.dashboard;

public interface IDashboardElement {
	String getId();
	
	int getX();
	
	int getY();
	
	int getW();
	
	int getH();
	
	int getPage();
	
	String getClientId();
	
	String getChannelId();
}

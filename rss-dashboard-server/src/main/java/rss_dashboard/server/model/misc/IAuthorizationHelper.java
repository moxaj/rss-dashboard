package rss_dashboard.server.model.misc;

public interface IAuthorizationHelper {
	void authorize(String data1, String data2) throws AuthorizationException;

	void deauthorize(String data) throws AuthorizationException;

	void reauthorize(ClientProfile clientProfile) throws AuthorizationException;
}

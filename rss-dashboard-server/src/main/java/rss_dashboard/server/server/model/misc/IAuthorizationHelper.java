package rss_dashboard.server.model.misc;

public interface IAuthorizationHelper {
	String authorize(String data) throws AuthorizationException;
	void deauthorize(String data) throws AuthorizationException;
}

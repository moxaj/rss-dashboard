package rss_dashboard.server.repository;

import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.rss.RssItem;

public class RssItemRepository extends AbstractRepository implements IRepository<RssItem> {
	@Override
	public void add(RssItem item) throws RepositoryException {
		List<RssItem> helper = new ArrayList<>();
		helper.add(item);
		
		add(helper);
	}

	@Override
	public void add(Iterable<RssItem> items) throws RepositoryException {
		
	}

	@Override
	public void update(RssItem item) throws RepositoryException {
		List<RssItem> helper = new ArrayList<>();
		helper.add(item);
		
		update(helper);
	}

	@Override
	public void update(Iterable<RssItem> items) throws RepositoryException {
		
	}

	@Override
	public void remove(RssItem item) throws RepositoryException {
		List<RssItem> helper = new ArrayList<>();
		helper.add(item);
		
		remove(helper);
	}

	@Override
	public void remove(Iterable<RssItem> items) throws RepositoryException {
		
	}

	@Override
	public List<RssItem> query(RssItem filter) throws RepositoryException {
		
		
		return null;
	}
}

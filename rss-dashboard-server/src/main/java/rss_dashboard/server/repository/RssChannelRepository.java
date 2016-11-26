package rss_dashboard.server.repository;

import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.rss.RssChannel;

public class RssChannelRepository extends AbstractRepository implements IRepository<RssChannel> {
	@Override
	public void add(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);
		
		add(helper);
	}

	@Override
	public void add(Iterable<RssChannel> items) throws RepositoryException {
		
	}

	@Override
	public void update(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);
		
		update(helper);
	}

	@Override
	public void update(Iterable<RssChannel> items) throws RepositoryException {
		
	}

	@Override
	public void remove(RssChannel item) throws RepositoryException {
		List<RssChannel> helper = new ArrayList<>();
		helper.add(item);
		
		remove(helper);
	}

	@Override
	public void remove(Iterable<RssChannel> items) throws RepositoryException {
		
	}

	@Override
	public List<RssChannel> query(RssChannel filter) throws RepositoryException {
		
		
		return null;
	}
}

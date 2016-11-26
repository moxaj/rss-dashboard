package rss_dashboard.server.repository;

import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.rss.RssCategory;

public class RssCategoryRepository extends AbstractRepository implements IRepository<RssCategory> {
	@Override
	public void add(RssCategory item) throws RepositoryException {
		List<RssCategory> helper = new ArrayList<>();
		helper.add(item);
		
		add(helper);
	}

	@Override
	public void add(Iterable<RssCategory> items) throws RepositoryException {
		
	}

	@Override
	public void update(RssCategory item) throws RepositoryException {
		List<RssCategory> helper = new ArrayList<>();
		helper.add(item);
		
		update(helper);
	}

	@Override
	public void update(Iterable<RssCategory> items) throws RepositoryException {
		
	}

	@Override
	public void remove(RssCategory item) throws RepositoryException {
		List<RssCategory> helper = new ArrayList<>();
		helper.add(item);
		
		remove(helper);
	}

	@Override
	public void remove(Iterable<RssCategory> items) throws RepositoryException {
		
	}

	@Override
	public List<RssCategory> query(RssCategory filter) throws RepositoryException {
		

		return null;
	}
}

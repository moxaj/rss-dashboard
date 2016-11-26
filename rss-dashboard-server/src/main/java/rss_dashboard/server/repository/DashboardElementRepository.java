package rss_dashboard.server.repository;

import java.util.ArrayList;
import java.util.List;

import rss_dashboard.server.model.dashboard.DashboardElement;

public class DashboardElementRepository extends AbstractRepository implements IRepository<DashboardElement> {
	@Override
	public void add(DashboardElement item) throws RepositoryException {
		List<DashboardElement> helper = new ArrayList<>();
		helper.add(item);

		add(helper);
	}

	@Override
	public void add(Iterable<DashboardElement> items) throws RepositoryException {

	}

	@Override
	public void update(DashboardElement item) throws RepositoryException {
		List<DashboardElement> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<DashboardElement> items) throws RepositoryException {

	}

	@Override
	public void remove(String id) throws RepositoryException {
		List<String> helper = new ArrayList<>();
		helper.add(id);

		remove(helper);
	}

	@Override
	public void remove(Iterable<String> ids) throws RepositoryException {

	}

	@Override
	public List<DashboardElement> query(DashboardElement filter) throws RepositoryException {

		return null;
	}
}

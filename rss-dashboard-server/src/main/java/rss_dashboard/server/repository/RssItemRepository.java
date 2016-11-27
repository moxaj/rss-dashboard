package rss_dashboard.server.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rss_dashboard.server.model.rss.RssItem;

public class RssItemRepository extends AbstractRepository implements IRepository<RssItem> {
	private static Map<String, RssItem> memoryRepository = new HashMap<>();

	@Override
	public String add(RssItem item) throws RepositoryException {
		List<RssItem> helper = new ArrayList<>();
		helper.add(item);

		return add(helper).get(0);
	}

	@Override
	public List<String> add(Iterable<RssItem> items) throws RepositoryException {
		List<String> returnIds = new ArrayList<>();

		for (RssItem item : items) {
			String id = randomId();

			memoryRepository.put(id, item);

			returnIds.add(id);
		}

		return returnIds;
	}

	@Override
	public void update(RssItem item) throws RepositoryException {
		List<RssItem> helper = new ArrayList<>();
		helper.add(item);

		update(helper);
	}

	@Override
	public void update(Iterable<RssItem> items) throws RepositoryException {
		for (RssItem item : items) {
			String id = item.getId();

			if (id == null || id.isEmpty()) {
				throw new RepositoryException("Id is null or empty.");
			}

			memoryRepository.put(id, item);
		}
	}

	@Override
	public void remove(String id) throws RepositoryException {
		List<String> helper = new ArrayList<>();
		helper.add(id);

		remove(helper);
	}

	@Override
	public void remove(Iterable<String> ids) throws RepositoryException {
		for (String id : ids) {
			memoryRepository.remove(id);
		}
	}

	@Override
	public List<RssItem> query(RssItem filter) throws RepositoryException {
		List<RssItem> typedResults = new ArrayList<>();

		String id = filter.getId();

		if (memoryRepository.containsKey(id)) {
			typedResults.add(memoryRepository.get(id));
		}

		return typedResults;
	}
}
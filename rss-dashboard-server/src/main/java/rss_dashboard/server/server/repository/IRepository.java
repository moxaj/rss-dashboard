package rss_dashboard.server.repository;

import java.util.List;

public interface IRepository<T> {
	void add(T item);
	void add(Iterable<T> items);
	void update(T item);
	void update(Iterable<T> items);
	void remove(T item);
	void remove(Iterable<T> items);
	List<T> query(String filter);
}

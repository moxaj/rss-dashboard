package rss_dashboard.server.repository;

import java.util.List;

public interface IRepository<T> {
	void add(T item) throws RepositoryException;

	void add(Iterable<T> items) throws RepositoryException;

	void update(T item) throws RepositoryException;

	void update(Iterable<T> items) throws RepositoryException;

	void remove(T item) throws RepositoryException;

	void remove(Iterable<T> items) throws RepositoryException;

	List<T> query(T filter) throws RepositoryException;
}

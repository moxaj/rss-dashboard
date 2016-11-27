package rss_dashboard.server.repository;

import java.util.List;

public interface IRepository<T> {
	String add(T item) throws RepositoryException;

	List<String> add(Iterable<T> items) throws RepositoryException;

	void update(T item) throws RepositoryException;

	void update(Iterable<T> items) throws RepositoryException;

	void remove(String id) throws RepositoryException;

	void remove(Iterable<String> ids) throws RepositoryException;

	List<T> query(T filter) throws RepositoryException;
}

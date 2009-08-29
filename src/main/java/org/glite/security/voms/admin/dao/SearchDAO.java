package org.glite.security.voms.admin.dao;

public class SearchDAO {

	public String query;
	public String allObjectsQuery;
	public String countQuery;
	public String allObjectsCountQuery;

	public String getAllObjectsCountQuery() {

		return allObjectsCountQuery;
	}

	public void setAllObjectsCountQuery(String allObjectsCountQuery) {

		this.allObjectsCountQuery = allObjectsCountQuery;
	}

	public String getAllObjectsQuery() {

		return allObjectsQuery;
	}

	public void setAllObjectsQuery(String allObjectsQuery) {

		this.allObjectsQuery = allObjectsQuery;
	}

	public String getCountQuery() {

		return countQuery;
	}

	public void setCountQuery(String countQuery) {

		this.countQuery = countQuery;
	}

	public String getQuery() {

		return query;
	}

	public void setQuery(String query) {

		this.query = query;
	}

	public SearchResults getAll() {
		return null;

	}

	public SearchResults getAll(int firstResult, int maxResults) {
		return null;
	}

	public SearchResults search(String searchParameter, int firstResult,
			int maxResults) {
		return null;
	}
}

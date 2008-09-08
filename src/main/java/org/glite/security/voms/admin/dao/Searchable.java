package org.glite.security.voms.admin.dao;


public interface Searchable {
    
    public SearchResults search(String text, int firstResult, int maxResults);
    public SearchResults getAll(int firstResult, int maxResults);
    public int countMatches(String text);
    

}

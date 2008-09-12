package it.cnaf.infn.voms.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;


public interface GenericDAO<T, ID extends Serializable> {
    
    T findByID(ID id, boolean lock);
    List<T> findAll();
    List<T> findByExample(T exampleInstance, String... exludeProperties);
    
    List<T> findByCriteria(DetachedCriteria criteria);
    
    T makePersistent(T entity);
    
    void makeTransient(T entity);
    
    void flush();
    
    void clear();

}

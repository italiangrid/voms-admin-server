package it.cnaf.infn.voms.dao;

import java.util.List;

import org.glite.security.voms.admin.database.HibernateFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class BaseGenericDAO<T, ID extends Serializable> implements
        GenericDAO <T, ID> {

    private Class <T> persistentClass;

    private Session session;

    @SuppressWarnings("unchecked")
    public BaseGenericDAO() {

        this.persistentClass = (Class <T>) ( (ParameterizedType) getClass()
                .getGenericSuperclass() ).getActualTypeArguments()[0];

    }

    public void setSession( Session session ) {

        this.session = session;
    }
    
    
    public Session getSession() {
        
        if (session == null){
            HibernateFactory.beginTransaction();
            session = HibernateFactory.getSession();
        }
            
        return session;
    }
    
    
    public Class <T> getPersistentClass() {

        return persistentClass;
    }
    
    
    public void clear() {

        getSession().clear();

    }

    public List <T> findAll() {

        return findByCriteria( null );
    }

    @SuppressWarnings("unchecked")
    public List <T> findByCriteria( DetachedCriteria criteria ) {

        Criteria c;
        
        if (criteria == null)
            c = getSession().createCriteria( getPersistentClass() );
        else
            c = criteria.getExecutableCriteria( getSession() );
        
        return c.list();
    }

    @SuppressWarnings("unchecked")
    public List <T> findByExample( T exampleInstance,
            String... excludeProperty ) {
        
        Criteria c = getSession().createCriteria( getPersistentClass() );
        Example example = Example.create( exampleInstance );
        
        for (String exclude: excludeProperty)
            example.excludeProperty( exclude );
        
        c.add( example );
        
        return c.list();
    }

    @SuppressWarnings("unchecked")
    public T findByID( ID id, boolean lock ) {

        T entity;
        
        if (lock)
            entity = (T) getSession().load( getPersistentClass(), id, LockMode.UPGRADE);
        else
            entity = (T) getSession().load( getPersistentClass(), id );
        
        return entity;
    }

    public void flush() {

        getSession().flush();

    }

    public T makePersistent( T entity ) {

        getSession().saveOrUpdate( entity );
        return entity;
    }

    public void makeTransient( T entity ) {

        getSession().delete( entity );
        
    }
}

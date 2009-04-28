package org.glite.security.voms.admin.dao.generic;

import java.io.Serializable;


public interface NamedEntityDAO<T, ID extends Serializable> {

    T findByName(String name);
    T deleteByName(String name);
    
}

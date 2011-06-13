package org.glite.security.voms.admin.operations;

import org.glite.security.voms.admin.persistence.HibernateFactory;

public abstract class GenericSaveOrUpdateOperation<T> extends BaseVomsOperation {

    T model;
    
    @Override
    protected Object doExecute() {
	
	HibernateFactory.getSession().saveOrUpdate(model);
	return model;
    }
    
    public GenericSaveOrUpdateOperation(T theModel) {
	model = theModel;
    }

}

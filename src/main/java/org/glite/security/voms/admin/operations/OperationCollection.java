package org.glite.security.voms.admin.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.common.VOMSException;

public abstract class OperationCollection {

	public static final Log log = LogFactory.getLog(OperationCollection.class);
	
	Class opClazz;
	List<VOMSOperation> operations;
	
	protected abstract void instantiateOperations() throws Exception;
	
	public final List<Object> execute(){
		
		try {
			
			instantiateOperations();
		
		} catch (Throwable e) {
			log.error("Error instantiating operation: "+e.getMessage());
			if (log.isDebugEnabled())
				log.error("Error instantiating operation: "+e.getMessage(), e);
			
			throw new VOMSException("Error instantiating operation: "+e.getMessage(), e);
		}
		
		
		List returnValues = new ArrayList();
		
		for (VOMSOperation op: operations){
			
			try{
				
				Object retVal = op.execute();
				returnValues.add(retVal);
				
			}catch (RuntimeException e) {
				log.error("Error executing operation '"+op.getName()+"' :"+e.getMessage());
				if (log.isDebugEnabled())
					log.error("Error executing operation '"+op.getName()+"' :"+e.getMessage(),e);
				
				throw e;
			}
		}
		
		return returnValues;
	}
	
}

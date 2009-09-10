package org.glite.security.voms.admin.operations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SingleArgumentOperationCollection<T> extends OperationCollection{

	public static final Log log = LogFactory.getLog(SingleArgumentOperationCollection.class);
	List<T> args;
	
	
	protected void instantiateOperations() throws Exception{
		
		operations = new ArrayList<VOMSOperation>();
		
		for (T arg: args){
			
			Method instanceMethod = opClazz.getMethod("instance", arg.getClass());
			operations.add((VOMSOperation)instanceMethod.invoke(null, arg));
		}
	}
	
	public SingleArgumentOperationCollection(List<T> args, Class opClass) {
		this.args = args;
		this.opClazz = opClass;
	}
	
}

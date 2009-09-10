package org.glite.security.voms.admin.operations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DoubleArgumentOperationCollection<T, U> extends OperationCollection{

	List<T> firstArgs;
	List<U> secondArgs;
	
	Class opClazz;

	public DoubleArgumentOperationCollection(List<T> args, List<U> secondArgs, Class opClass) {
		
		this.firstArgs = args;
		this.secondArgs = secondArgs;
		this.opClazz = opClass;
	}
	
	@Override
	protected void instantiateOperations() throws Exception {
		
		operations = new ArrayList<VOMSOperation>();
		
		if (firstArgs.size() != secondArgs.size())
			throw new IllegalArgumentException("first argument lists and second argument lists must be of the same size!");
			
		for (int i = 0; i <firstArgs.size(); i++){
			
			T firstArg = firstArgs.get(i);
			U secondArg = secondArgs.get(i);
			
			Method instanceMethod = opClazz.getMethod("instance", firstArg.getClass(), secondArg.getClass());
			operations.add((VOMSOperation)instanceMethod.invoke(null, firstArg,secondArg));
			
		}
		
	}
	
}

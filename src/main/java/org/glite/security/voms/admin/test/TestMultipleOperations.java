package org.glite.security.voms.admin.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.operations.groups.DeleteGroupOperation;

public class TestMultipleOperations {

	
	protected VOMSOperation createOperation(Class opClass, Long id) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		
		Method instanceMethod = opClass.getMethod("instance", new Class[]{Long.class});
		
		VOMSOperation o = (VOMSOperation) instanceMethod.invoke(null, id);
		
		
		return o;
	}
	
	
	public TestMultipleOperations() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		
		VOMSOperation o = createOperation(DeleteGroupOperation.class, 5L);
		o.execute();
		
		
		
	}
	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		new TestMultipleOperations();
		

	}

}

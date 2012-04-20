package org.glite.security.voms.admin.view.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;

public class JSONExceptionReportInterceptor extends
		JSONValidationReportInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		
		try{
			
			String result = invocation.invoke();
			
			return result;
		
		}catch (Throwable t){
			
			log.debug("Caught {} while executing action {}", t, invocation.getAction());
			log.debug(t.getMessage(), t);
			
			return generateJSON((ValidationAware) invocation.getAction(), t);
			
		}
	}
}

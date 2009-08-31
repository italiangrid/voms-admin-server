package org.glite.security.voms.admin.view.interceptors;

import javax.servlet.http.Cookie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.glite.security.voms.admin.common.VOMSAuthorizationException;
import org.glite.security.voms.admin.view.actions.AuthorizationErrorAware;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor;

public class AuthzExceptionInterceptor extends ExceptionMappingInterceptor {

	public static final String ERROR_KEY = "voms-authorization-error";
	public static final Log log = LogFactory
			.getLog(AuthzExceptionInterceptor.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation ai) throws Exception {

		String result;
		try {

			result = ai.invoke();
			return result;

		} catch (Exception e) {

			if (e instanceof VOMSAuthorizationException) {

				log.debug("Caught VOMS authorization exception: " + e);
				
				if (ai.getAction() instanceof ValidationAware){
					
					ValidationAware vaAction = (ValidationAware)ai.getAction();
					vaAction.addActionError(e.getMessage());
					
				}
				
				if (ai.getAction() instanceof AuthorizationErrorAware) {

					AuthorizationErrorAware aeaAction = (AuthorizationErrorAware) ai
							.getAction();
					return aeaAction.getAuthorizationErrorResult();

				}
				return BaseAction.INPUT;
			}

			// Fall back to standard exception mapping mechanism for other
			// exceptions
			throw e;
		}

	}
}

class AuthzExceptionHolder {

	VOMSAuthorizationException authorizationException;

	public AuthzExceptionHolder(VOMSAuthorizationException e) {
		authorizationException = e;
	}

	public VOMSAuthorizationException getAuthorizationException() {
		return authorizationException;
	}

}

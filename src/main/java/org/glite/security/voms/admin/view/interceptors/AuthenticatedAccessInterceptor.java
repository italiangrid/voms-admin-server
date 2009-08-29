package org.glite.security.voms.admin.view.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.common.InitSecurityContext;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.operations.CurrentAdmin;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthenticatedAccessInterceptor extends AbstractInterceptor
		implements StrutsStatics {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory
			.getLog(AuthenticatedAccessInterceptor.class);

	public void destroy() {

		// TODO Auto-generated method stub

	}

	public void init() {

		// TODO Auto-generated method stub

	}

	public String intercept(ActionInvocation ai) throws Exception {

		HttpServletRequest req = (HttpServletRequest) ai.getInvocationContext()
				.get(HTTP_REQUEST);
		InitSecurityContext.setContextFromRequest(req);
		req.setAttribute("voName", VOMSConfiguration.instance().getVOName());
		req.setAttribute(VOMSServiceConstants.CURRENT_ADMIN_KEY, CurrentAdmin
				.instance());

		return ai.invoke();
	}

}

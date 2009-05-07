package org.glite.security.voms.admin.actions.aup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;

public class TestAction extends BaseAction {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String[] data = new String[]{"1","2","3"};
		
		request.setAttribute("data", data);
		
		return findSuccess(mapping);
	}

}

package org.glite.security.voms.admin.actions.generic;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.glite.security.voms.admin.actions.BaseAction;
import org.glite.security.voms.admin.dao.generic.GenericDAO;

public class LoadModelAction<T, ID extends Serializable, DAO extends GenericDAO<T, ID>> extends BaseAction {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		 
		
		return super.execute(mapping, form, request, response);
	}
}

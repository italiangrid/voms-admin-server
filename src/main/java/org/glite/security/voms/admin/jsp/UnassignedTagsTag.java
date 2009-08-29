package org.glite.security.voms.admin.jsp;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.model.VOMSAdmin;

public class UnassignedTagsTag extends TagSupport {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String adminId;
	String var;

	public String getAdminId() {

		return adminId;
	}

	public void setAdminId(String adminId) {

		this.adminId = adminId;
	}

	public String getVar() {

		return var;
	}

	public void setVar(String var) {

		this.var = var;
	}

	@Override
	public int doStartTag() throws JspException {
		// FIXME: DO this with an operation
		// List<VOMSAdmin> unassignedTags =
		// VOMSAdminDAO.instance().getUnassignedTagsForAdmin( new Long(adminId)
		// );
		// pageContext.setAttribute( var, unassignedTags);
		//  
		return SKIP_BODY;
	}

}

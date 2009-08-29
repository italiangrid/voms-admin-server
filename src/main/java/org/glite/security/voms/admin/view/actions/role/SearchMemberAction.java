package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.search.BaseSearchAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.SUCCESS, location = "searchMember.jsp"),
		@Result(name = BaseAction.INPUT, location = "searchMemberError.jsp") })
public class SearchMemberAction extends BaseSearchAction implements
		ModelDriven<VOMSRole>, Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VOMSGroup group;
	VOMSRole role;

	public void prepare() throws Exception {
		initSearchData(ROLE_MEMBER_SEARCH_NAME);

		group = groupById(searchData.getGroupId());
		role = roleById(searchData.getRoleId());

	}

	public VOMSGroup getGroup() {
		return group;
	}

	public VOMSRole getModel() {

		return role;
	}

}

package org.glite.security.voms.admin.view.actions.role;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.search.BaseSearchAction;

import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( {

@Result(name = BaseAction.SUCCESS, location = "roles"),
		@Result(name = BaseAction.INPUT, location = "roles") })
public class SearchAction extends BaseSearchAction implements Preparable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void prepare() throws Exception {

		initSearchData(ROLE_SEARCH_NAME);

	}
}

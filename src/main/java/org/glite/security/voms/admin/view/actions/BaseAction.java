/**
 * 
 */
package org.glite.security.voms.admin.view.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author andrea
 *
 */


public class BaseAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String INPUT_FORM_REGEX = "^[^<>&=;]*$";
	public static final String INPUT_DN_REGEX = "^[^<>&;]*$";
	
	public static final String SEARCH = "search";
	public static final String LOAD = "load";
	public static final String LIST = "list";
	public static final String EDIT = "edit";
	
	public static final String CREATE_FORM = "create-form";
	
	
	protected VOMSUser userById(Long id){
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");
		
		return (VOMSUser)FindUserOperation.instance(id).execute();
	}
	
	
	protected VOMSGroup groupByName(String name){
		
		if (name == null)
			throw new NullArgumentException("'name' cannot be null!");
		
		return (VOMSGroup)FindGroupOperation.instance(name).execute();
		
	}
	protected VOMSGroup groupById(Long id){
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");
		
		return (VOMSGroup)FindGroupOperation.instance(id).execute();
	}
	
	protected VOMSRole roleById(Long id){
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");
		
		return (VOMSRole)FindRoleOperation.instance(id).execute();
	}
	
	protected VOMSRole roleByName(String name){
		
		if (name == null)
			throw new NullArgumentException("'name' cannot be null!");
		
		return (VOMSRole)FindRoleOperation.instance(name).execute();
	}
	
	@Action(value=INPUT)
	public String input() throws Exception{
		
		return INPUT;
	}
	
	@SkipValidation
	@Action(value=CREATE_FORM)
	public String createForm() throws Exception{
		
		return CREATE_FORM;
	}
	
	@Action(value=EDIT)
	public String edit() throws Exception{
		
		return EDIT;
	}
	
	

}

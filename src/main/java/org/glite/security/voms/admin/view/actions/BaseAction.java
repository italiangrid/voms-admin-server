/**
 * 
 */
package org.glite.security.voms.admin.view.actions;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.glite.security.voms.admin.common.NullArgumentException;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;

import com.opensymphony.xwork2.ActionContext;
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
	public static final String LIST = "list";
	public static final String EDIT = "edit";

	public static final String CREATE = "create";
	public static final String SAVE = "save";

	public static final String AUTHZ_ERROR = "authorizationError";

	protected VOMSUser userById(Long id) {
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");

		return (VOMSUser) FindUserOperation.instance(id).execute();
	}

	protected VOMSGroup groupByName(String name) {

		if (name == null)
			throw new NullArgumentException("'name' cannot be null!");

		return (VOMSGroup) FindGroupOperation.instance(name).execute();

	}

	protected VOMSGroup groupById(Long id) {
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");

		return (VOMSGroup) FindGroupOperation.instance(id).execute();
	}

	protected VOMSRole roleById(Long id) {
		if (id == null)
			throw new NullArgumentException("'id' cannot be null!");

		return (VOMSRole) FindRoleOperation.instance(id).execute();
	}

	protected VOMSRole roleByName(String name) {

		if (name == null)
			throw new NullArgumentException("'name' cannot be null!");

		return (VOMSRole) FindRoleOperation.instance(name).execute();
	}

	protected String getBaseURL() {

		HttpServletRequest req = ServletActionContext.getRequest();

		String result = req.getScheme() + "://" + req.getServerName() + ":"
				+ req.getServerPort() + "/voms/"
				+ VOMSConfiguration.instance().getVOName();

		return result;
	}

	protected Date getFutureDate(Date initialDate, int field, int increment) {
		Calendar c = Calendar.getInstance();
		c.setTime(initialDate);
		c.add(field, increment);

		return c.getTime();
	}

}

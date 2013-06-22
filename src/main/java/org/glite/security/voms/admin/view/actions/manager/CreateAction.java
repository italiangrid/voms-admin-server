package org.glite.security.voms.admin.view.actions.manager;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.group_manager.CreateGroupManagerOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.model.GroupManager;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
	@Result(name = BaseAction.SUCCESS, location="index", type="redirectAction"),
	@Result(name = BaseAction.INPUT, location="managerCreate")
})

@InterceptorRef(value = "authenticatedStack", params = {
	"token.includeMethods", "execute" })
public class CreateAction extends BaseAction implements Preparable{

	private static final long serialVersionUID = 1L;

	Long groupId;
	
	String name;
	String description;
	String emailAddress;
	
	List<VOMSGroup> voGroups;
	
	VOMSGroup group;
	
	@Override
	public void validate() {
	
		group = groupById(getGroupId());
		
		if (group == null)
			addFieldError("groupId", "'"+getGroupId()+"' is not a valid group id!");
		
	}
	
	@Override
	public String execute() throws Exception {
				
		GroupManager gm = new GroupManager();
	
		gm.setName(name);
		gm.setDescription(description);
		gm.setEmailAddress(emailAddress);
		gm.getManagedGroups().add(group);
		
		new CreateGroupManagerOperation(gm).execute();
		
		addActionMessage("Manager created succesfully: "+gm.getName());
		
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {

		voGroups = VOMSGroupDAO.instance().findAllWithoutRootGroup();
	}

	
	/**
	 * @return the voGroups
	 */
	public List<VOMSGroup> getVoGroups() {
	
		return voGroups;
	}
	
	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
	
		return groupId;
	}

	
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
	
		this.groupId = groupId;
	}
	
	
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "Please enter an email address for the manager.")
	@EmailValidator(type = ValidatorType.FIELD, message = "Please enter a valid email address.")
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The email field name contains illegal characters!", expression = "^[^<>&=;]*$")
	public String getEmailAddress() {
	
		return emailAddress;
	}
	
	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
	
		this.emailAddress = emailAddress;
	}

		
	@RequiredStringValidator(type = ValidatorType.FIELD, message = "A name for the manager is required!")
	@RegexFieldValidator(type = ValidatorType.FIELD, 
		message = "The manager name field contains illegal characters!", 
		expression = "^[^<>&=;]*$")
	public String getName() {
	
		return name;
	}

	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
	
		this.name = name;
	}

	
	@RegexFieldValidator(type = ValidatorType.FIELD, message = "The description field contains illegal characters!", expression = "^[^<>&=;]*$")
	@StringLengthFieldValidator(type = ValidatorType.FIELD, 
		maxLength="255", 
		message="The description field size is limited to 255 characters.")
	public String getDescription() {
	
		return description;
	}

	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
	
		this.description = description;
	}	
	
	
}

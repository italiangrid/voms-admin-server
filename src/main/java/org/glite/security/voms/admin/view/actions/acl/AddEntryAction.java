package org.glite.security.voms.admin.view.actions.acl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.dao.VOMSCADAO;
import org.glite.security.voms.admin.dao.VOMSUserDAO;
import org.glite.security.voms.admin.database.NoSuchUserException;
import org.glite.security.voms.admin.model.VOMSCA;
import org.glite.security.voms.admin.model.VOMSGroup;
import org.glite.security.voms.admin.model.VOMSRole;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.operations.VOMSPermission;
import org.glite.security.voms.admin.operations.acls.SaveACLEntryOperation;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;
import com.opensymphony.xwork2.validator.validators.EmailValidator;


@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.SUCCESS, location = "manage", type = "chain"),
		@Result(name = BaseAction.INPUT, location = "addACLEntry") })
public class AddEntryAction extends ACLActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String entryType;

	List<String> selectedPermissions;

	VOMSPermission permission;

	Long userId;

	String dn;

	Short caId;

	String emailAddress;

	Long roleId;

	Long roleGroupId;

	Long groupId;
	
	Map<String, String> entryTypeMap;

	@Override
	public void validate() {
		if (selectedPermissions == null)
			addActionError("No permissions selected!");
		else if (selectedPermissions.isEmpty())
			addActionError("No permissions selected!");
		
		if (hasActionErrors())
			buildEntryTypeMap();
		
		if (entryType.equals("non-vo-user")){
			
			if (dn == null || "".equals(dn))
				addFieldError("dn", "Please specify a subject!");
			
			if (emailAddress == null || "".equals(emailAddress))
				addFieldError("emailAddress", "Please specify a valid email address!");
			
		}
	}
	private void loadAdmin() throws Exception {

		if (entryType.equals("vo-user")) {

			VOMSUser u = (VOMSUser) FindUserOperation.instance(userId)
					.execute();
			if (u == null)
				throw new NoSuchUserException("User not found for id : "
						+ userId);

			admin = VOMSAdminDAO.instance().createFromUser(u);

		} else if (entryType.equals("non-vo-user")) {

			VOMSCA ca = VOMSCADAO.instance().getByID(caId);
			admin = VOMSAdminDAO.instance()
					.getByName(dn, ca.getSubjectString());

			if (admin == null)
				admin = VOMSAdminDAO.instance().create(dn, ca.getSubjectString());

		} else if (entryType.equals("role-admin")) {

			VOMSRole r = (VOMSRole) FindRoleOperation.instance(roleId)
					.execute();
			VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(roleGroupId)
					.execute();

			VOMSContext ctxt = VOMSContext.instance(g, r);

			admin = VOMSAdminDAO.instance().getByFQAN(ctxt.toString());
			if (admin == null)
				admin = VOMSAdminDAO.instance().create(ctxt.toString());

		} else if (entryType.equals("group-admin")) {

			VOMSGroup g = (VOMSGroup) FindGroupOperation.instance(groupId)
					.execute();
			VOMSContext ctxt = VOMSContext.instance(g);

			admin = VOMSAdminDAO.instance().getByFQAN(ctxt.toString());

			if (admin == null)
				admin = VOMSAdminDAO.instance().create(ctxt.toString());

		} else if (entryType.equals("anyone")) {

			admin = VOMSAdminDAO.instance().getAnyAuthenticatedUserAdmin();

		} else
			throw new IllegalArgumentException("Unsupported entryType value: "
					+ entryType);
	}

	@Override
	public String execute() throws Exception {

		loadAdmin();

		SaveACLEntryOperation op = SaveACLEntryOperation.instance(getModel(),
				admin, VOMSPermission.fromStringArray(selectedPermissions
						.toArray(new String[selectedPermissions.size()])),
				propagate == null ? false : propagate);

		op.execute();

		return SUCCESS;
	}

	public List<String> getSelectedPermissions() {
		return selectedPermissions;
	}

	public void setSelectedPermissions(List<String> selectedPermissions) {
		this.selectedPermissions = selectedPermissions;
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "entryType is required!")
	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {

		this.entryType = entryType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Short getCaId() {
		return caId;
	}

	public void setCaId(Short caId) {
		this.caId = caId;
	}

	public Long getRoleGroupId() {
		return roleGroupId;
	}

	public void setRoleGroupId(Long roleGroupId) {
		this.roleGroupId = roleGroupId;
	}

	public VOMSPermission getPermission() {
		return permission;
	}

	public void setPermission(VOMSPermission permission) {
		this.permission = permission;
	}

	public void prepareInput() throws Exception {

		prepare();

		if (permission == null)
			permission = VOMSPermission.getEmptyPermissions();
		
		buildEntryTypeMap();
		
	}

	protected void buildEntryTypeMap(){
		
		entryTypeMap = new LinkedHashMap<String, String>();
		
		entryTypeMap.put("anyone", "any authenticated user");
		
		if (!VOMSUserDAO.instance().getAll().isEmpty())
			entryTypeMap.put("vo-user", "a VO member certificate");
		
		entryTypeMap.put("role-admin","VO members with a specific role");
		entryTypeMap.put("group-admin", "VO members in a specific group");
		entryTypeMap.put("non-vo-user", "a non VO member");
	}
	
	public Map<String, String> getEntryTypeMap() {
		return entryTypeMap;
	}
	
}

package org.glite.security.voms.admin.view.actions;

import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

public class SearchData {

	String text;
	
	String type;
	
	int firstResult;
	int maxResults;
	
	Long groupId;
	
	Long roleId;
	
	public SearchData() {
		// TODO Auto-generated constructor stub
	}

	@RegexFieldValidator(type=ValidatorType.FIELD, message="the search text field contains illegal characters!", expression="^[^<>&=;]*$")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
}

package org.glite.security.voms.admin.view.actions.apiv2;

import java.util.List;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.json.annotations.JSON;
import org.glite.security.voms.admin.apiv2.JSONSerializer;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.users.FindUsersWithAttribute;
import org.glite.security.voms.admin.operations.users.FindUsersWithAttributeValue;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("json")
@Results({ @Result(name = BaseAction.SUCCESS, type = "json") })
public class UserAttributesAction extends BaseAction {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  String attributeName;
  String attributeValue;

  List<VOMSUserJSON> userWithAttributes;
  

  @RequiredStringValidator(type = ValidatorType.FIELD,
    message = "Please specify an attribute name.")
  @JSON(serialize=false)
  public String getAttributeName() {

    return attributeName;
  }

  public void setAttributeName(String attributeName) {

    this.attributeName = attributeName;
  }

  @StringLengthFieldValidator(type= ValidatorType.FIELD,
    maxLength="255", minLength="1", 
    message="Please provide an attribute value string whose lenght is in "
      + "the 1,255 range")
  @JSON(serialize=false)
  public String getAttributeValue() {

    return attributeValue;
  }

  public void setAttributeValue(String attributeValue) {

    this.attributeValue = attributeValue;
  }

  public List<VOMSUserJSON> getUserWithAttributes() {

    return userWithAttributes;
  }

  @Override
  public String execute() throws Exception {
  
    List<VOMSUser> users;
    
    if (attributeValue == null){
      users = new FindUsersWithAttribute(attributeName).execute();
    } else {
      users = new FindUsersWithAttributeValue(attributeName, attributeValue)
        .execute();
    }
    
    userWithAttributes = JSONSerializer.serialize(users);
    return BaseAction.SUCCESS;
  }
}

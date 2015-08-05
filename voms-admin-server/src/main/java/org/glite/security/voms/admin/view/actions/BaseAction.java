/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
/**
 * 
 */
package org.glite.security.voms.admin.view.actions;

import java.util.Calendar;
import java.util.Date;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.error.NullArgumentException;
import org.glite.security.voms.admin.operations.groups.FindGroupOperation;
import org.glite.security.voms.admin.operations.roles.FindRoleOperation;
import org.glite.security.voms.admin.operations.users.FindUserOperation;
import org.glite.security.voms.admin.persistence.dao.VOMSAdminDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSGroupDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSRoleDAO;
import org.glite.security.voms.admin.persistence.model.VOMSAdmin;
import org.glite.security.voms.admin.persistence.model.VOMSGroup;
import org.glite.security.voms.admin.persistence.model.VOMSRole;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.util.URLBuilder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;

public class BaseAction extends ActionSupport implements ValidationAware {

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

  protected VOMSAdmin adminById(Long id) {

    if (id == null)
      throw new NullArgumentException("'id' cannot be null!");

    return VOMSAdminDAO.instance().getById(id);

  }

  protected VOMSGroup getVORootGroup() {

    return VOMSGroupDAO.instance().getVOGroup();
  }

  protected VOMSRole getGroupManagerRole() {

    return VOMSRoleDAO.instance().findByName(VOMSConfiguration
      .instance().getGroupManagerRoleName());
    
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

  protected String getHomeURL() {

    return URLBuilder.baseVOMSURLFromConfiguration() + "/admin/home.action";
  }

  protected Date getFutureDate(Date initialDate, int field, int increment) {

    Calendar c = Calendar.getInstance();
    c.setTime(initialDate);
    c.add(field, increment);

    return c.getTime();
  }

  protected Date getDefaultFutureDate() {

    return getFutureDate(new Date(), Calendar.DAY_OF_YEAR, 7);
  }
}

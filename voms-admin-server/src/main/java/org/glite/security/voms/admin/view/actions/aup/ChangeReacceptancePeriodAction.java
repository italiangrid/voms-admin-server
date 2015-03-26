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
package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.aup.ChangeReacceptancePeriodOperation;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Results({
  @Result(name = BaseAction.INPUT, location = "aups"),
  @Result(name = BaseAction.SUCCESS, location = "/aup/load.action",
    type = "redirect") })
@InterceptorRef(value = "authenticatedStack", params = {
  "token.includeMethods", "execute" })
public class ChangeReacceptancePeriodAction extends BaseAction implements
  Preparable, ModelDriven<AUP> {

  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;

  Long aupId;
  int period;

  AUP aup;

  @Override
  public String execute() throws Exception {

    ChangeReacceptancePeriodOperation op = new ChangeReacceptancePeriodOperation(
      aup, period);
    op.execute();

    return SUCCESS;
  }

  public Long getAupId() {

    return aupId;
  }

  public void setAupId(Long aupId) {

    this.aupId = aupId;
  }

  @RequiredFieldValidator(type = ValidatorType.FIELD,
    message = "The period (in days) is required.")
  @RegexFieldValidator(type = ValidatorType.FIELD, regex = "[0-9]+",
    message = "Please specify a positive integer number.")
  @IntRangeFieldValidator(type = ValidatorType.FIELD, min = "1", max = "730",
    message = "The input is out of the acceptable range (1 < x < 730)")
  public int getPeriod() {

    return period;
  }

  public void setPeriod(int period) {

    this.period = period;
  }

  public void prepare() throws Exception {

    if (aup == null) {
      AUPDAO dao = DAOFactory.instance().getAUPDAO();

      aup = dao.findById(getAupId(), true);

    }

  }

  public AUP getModel() {

    return aup;
  }

}

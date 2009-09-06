package org.glite.security.voms.admin.view.actions.aup;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.operations.aup.ChangeReacceptancePeriodOperation;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.IntRangeFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RegexFieldValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@ParentPackage("base")
@Results( {
		@Result(name = BaseAction.INPUT, location = "aups"),
		@Result(name = BaseAction.SUCCESS, location = "/aup/load.action", type = "redirect") })
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

		ChangeReacceptancePeriodOperation op = new ChangeReacceptancePeriodOperation(aup, period);
		op.execute();
		
		return SUCCESS;
	}

	public Long getAupId() {
		return aupId;
	}

	public void setAupId(Long aupId) {
		this.aupId = aupId;
	}

	@RequiredFieldValidator(type = ValidatorType.FIELD, message = "The period (in days) is required.")
	@RegexFieldValidator(type = ValidatorType.FIELD, expression = "[0-9]+", message = "Please specify a positive integer number.")
	@IntRangeFieldValidator(type = ValidatorType.FIELD, min = "1", max = "730", message = "The input is out of the acceptable range (1 < x < 730)")
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

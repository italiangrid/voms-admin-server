package org.glite.security.voms.admin.view.actions.register;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.views.freemarker.tags.SetModel;
import org.glite.security.voms.admin.common.VOMSConfiguration;
import org.glite.security.voms.admin.dao.generic.AUPDAO;
import org.glite.security.voms.admin.dao.generic.DAOFactory;
import org.glite.security.voms.admin.dao.generic.RequestDAO;
import org.glite.security.voms.admin.model.AUPVersion;
import org.glite.security.voms.admin.model.request.NewVOMembershipRequest;
import org.glite.security.voms.admin.model.request.RequesterInfo;
import org.glite.security.voms.admin.model.request.Request.StatusFlag;
import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.user.UserPersonalInfo;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@ParentPackage("base")
@Results( { @Result(name = BaseAction.INPUT, location = "register"),
		@Result(name = BaseAction.SUCCESS, location = "registerConfirmation")})
public abstract class RegisterActionSupport extends BaseAction implements
		Preparable, ModelDriven<NewVOMembershipRequest> {

	public static final String CONFIRMATION_NEEDED = "needToConfirm";
	public static final String PLEASE_WAIT = "pleaseWait";
	public static final String ALREADY_MEMBER = "alreadyAMember";
	public static final String REGISTRATION_DISABLED = "registrationDisabled";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Long requestId = -1L;

	NewVOMembershipRequest request;

	RequesterInfo requester;

	AUPVersion currentAUPVersion;
	
	protected String checkExistingPendingRequests() {

		RequestDAO dao = DAOFactory.instance().getRequestDAO();

		NewVOMembershipRequest req = dao
				.findActiveVOMembershipRequest(requester);

		if (req != null) {

			request = req;
			if (req.getStatus().equals(StatusFlag.SUBMITTED))
				return CONFIRMATION_NEEDED;

			if (req.getStatus().equals(StatusFlag.CONFIRMED)
					|| req.getStatus().equals(StatusFlag.PENDING))
				return PLEASE_WAIT;

		}
		return null;
	}

	protected RequesterInfo requesterInfoFromCurrentAdmin() {

		RequesterInfo i = new RequesterInfo();
		CurrentAdmin admin = CurrentAdmin.instance();
		i.setCertificateSubject(admin.getRealSubject());
		i.setCertificateIssuer(admin.getRealIssuer());
		i.setEmailAddress(admin.getRealEmailAddress());

		return i;

	}

	public void prepare() throws Exception {

		requester = requesterInfoFromCurrentAdmin();
		
		AUPDAO aupDAO = DAOFactory.instance().getAUPDAO();

		currentAUPVersion = aupDAO.getVOAUP().getActiveVersion();
		
		if (getModel() == null) {

			if (requestId != -1L) {
				RequestDAO dao = DAOFactory.instance().getRequestDAO();
				request = (NewVOMembershipRequest) dao
						.findById(requestId, true);
			}
		}

	}

	public NewVOMembershipRequest getModel() {

		return request;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public RequesterInfo getRequester() {
		return requester;
	}

	public void setRequester(RequesterInfo requester) {
		this.requester = requester;
	}

	public boolean registrationEnabled() {
		return VOMSConfiguration.instance().getBoolean(
				VOMSConfiguration.REGISTRATION_SERVICE_ENABLED, true);
	}

	public AUPVersion getCurrentAUPVersion() {
		return currentAUPVersion;
	}
	
}

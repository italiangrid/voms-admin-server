package org.glite.security.voms.admin.view.actions.audit;

import java.util.Calendar;
import java.util.concurrent.Callable;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.glite.security.voms.admin.operations.VOAdminOperation;
import org.glite.security.voms.admin.operations.audit.SearchAuditLogEvents;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.view.actions.BaseAction;
import org.glite.security.voms.admin.view.actions.audit.util.AdminNameFormatter;
import org.glite.security.voms.admin.view.actions.audit.util.AuditEventUtils;
import org.glite.security.voms.admin.view.actions.audit.util.EventDataPointClassifier;
import org.glite.security.voms.admin.view.actions.audit.util.EventDescriptor;
import org.glite.security.voms.admin.view.actions.audit.util.EventNameFormatter;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

@Results({ @Result(name = BaseAction.SUCCESS, location = "auditLog") })
public class IndexAction extends BaseAction implements
  ModelDriven<AuditLogSearchResults>, Preparable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  AuditLogSearchParams searchParams;

  AuditLogSearchResults results;

  @Override
  public void prepare() throws Exception {

    final Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, -7);

    if (searchParams == null) {

      searchParams = new AuditLogSearchParams();
      searchParams.setFromTime(cal.getTime());

    } else {
      if (searchParams.getFromTime() == null) {
        searchParams.setFromTime(cal.getTime());

      }
    }

  }

  @Override
  public String execute() throws Exception {

    Callable<AuditLogSearchResults> task = new SearchAuditLogEvents(
      searchParams, DAOFactory.instance().getAuditSearchDAO());

    VOAdminOperation<AuditLogSearchResults> op = new VOAdminOperation<AuditLogSearchResults>(
      task);

    results = op.execute();

    return SUCCESS;
  }

  @Override
  public AuditLogSearchResults getModel() {

    return results;
  }

  public AuditLogSearchParams getSearchParams() {

    return searchParams;
  }

  public void setSearchParams(AuditLogSearchParams searchParams) {

    this.searchParams = searchParams;
  }

  public EventNameFormatter getEventNameFormatter() {

    return AuditEventUtils.eventNameFormatter();
  }

  public AdminNameFormatter getAdminNameFormatter() {

    return AuditEventUtils.adminNameFormatter();
  }
  
  public EventDescriptor getEventDescriptor(){
    return AuditEventUtils.eventDescriptor();
  }
}

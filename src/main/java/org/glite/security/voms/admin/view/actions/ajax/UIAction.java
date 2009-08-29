package org.glite.security.voms.admin.view.actions.ajax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.glite.security.voms.admin.common.VOMSServiceConstants;
import org.glite.security.voms.admin.view.actions.BaseAction;


@ParentPackage("json")
@Results({
	@Result(name = BaseAction.SUCCESS, type="json")
})
public class UIAction extends BaseAction implements SessionAware{
	
	public static final Log log = LogFactory.getLog(UIAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String panelId;
	Boolean visible;
	
	Map<String,Boolean> statusMap;
	
	private transient Map<String, Object> session;
	
	@Action("store")
	public String store(){
		
		log.debug("panelId: " + panelId);
		log.debug("visible" + visible);
		
		statusMap = (HashMap<String, Boolean>) session.get(VOMSServiceConstants.STATUS_MAP_KEY);
		
		if (statusMap == null)
			statusMap = new HashMap<String,Boolean>();
		
		if (panelId != null)
			statusMap.put(panelId, visible);
		
		session.put(VOMSServiceConstants.STATUS_MAP_KEY, statusMap);
		
		return SUCCESS;
	}
	
	@Action("status")
	public String status(){
		
		statusMap = (HashMap<String, Boolean>) session.get(VOMSServiceConstants.STATUS_MAP_KEY);
		
		return SUCCESS;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;		
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Map<String, Boolean> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, Boolean> statusMap) {
		this.statusMap = statusMap;
	}
	
}

/**
 * 
 */
package org.glite.security.voms.admin.view.actions;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.glite.security.voms.admin.dao.generic.GenericDAO;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author andrea
 *
 */
public abstract class BaseCRUDAction<T, ID extends Serializable> extends ActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum RequestType{
		list,
		save,
		update,
		remove
	}
	
	protected Log log = LogFactory.getLog(BaseCRUDAction.class);
	
	protected GenericDAO<T, ID> dao;
	protected long requestId;
	protected boolean readOnly = false;
	protected String mappedRequest;
	
	protected T model;
	
	@SkipValidation
	public String show(){
		
		setReadOnly(true);
		setMappedRequest(RequestType.list);
		return SUCCESS;
		
	}
	
	@SkipValidation
	public String add(){
		setMappedRequest(RequestType.save);
		return SUCCESS;
	}
	
	public String list(){
		
		// Code to fetch list of objects and populate request
		// for the view is the Tiles 'preparer'
		
		log.debug(getActionClass()+":list");
		setMappedRequest(RequestType.list);
		return RequestType.list.toString();
		
	}
	
	public String save(){
		log.debug(getActionClass()+":save");
		getDao().makePersistent(getModel());
		return list();	
	}
	
	@SkipValidation
	public String edit(){
		log.debug(getActionClass()+":edit");
		setMappedRequest(RequestType.update);
		return SUCCESS;
	}
	
	public String update(){
		log.debug(getActionClass()+":update");
		getDao().makePersistent(getModel());
		return list();
		
	}
	
	@SkipValidation
	public String destroy(){
		log.debug(getActionClass()+":destroy");
		setReadOnly(true);
		setMappedRequest(RequestType.remove);
		return SUCCESS;
	}
	
	public String remove(){
		log.debug(getActionClass()+":remove");
		getDao().makeTransient(getModel());
		return list();
	}
	
	public String getActionClass(){
		
		return getClass().getSimpleName();
	}
	
	public String getDestination(){
		return getClass().getSimpleName();
	}
	
	public String getActionMethod(){
		return mappedRequest;
	}
	
	public void setActionMethod(String method){
		this.mappedRequest = method;
	}

	public GenericDAO<T, ID> getDao() {
		return dao;
	}

	public void setDao(GenericDAO<T, ID> dao) {
		log.debug("Setting DAO for concrete class '"+getActionClass()+"'");
		this.dao = dao;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getMappedRequest() {
		return mappedRequest;
	}

	public void setMappedRequest(RequestType requestType) {
		
		this.mappedRequest = getActionClass()+"_"+requestType.toString();
		
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}
	
	
	
}

package org.glite.security.voms.admin.dao.generic;

import java.util.Date;
import java.util.List;

import org.glite.security.voms.admin.model.AUP;
import org.glite.security.voms.admin.model.VOMSAdmin;
import org.glite.security.voms.admin.model.VOMSUser;
import org.glite.security.voms.admin.model.request.Request;
import org.glite.security.voms.admin.model.task.ApproveUserRequestTask;
import org.glite.security.voms.admin.model.task.SignAUPTask;
import org.glite.security.voms.admin.model.task.Task;



public interface TaskDAO extends GenericDAO <Task, Long>{

    SignAUPTask createSignAUPTask(AUP aup, Date expiryDate);
        
    ApproveUserRequestTask createApproveUserRequestTask(Request req);
    
    void setSignAUPTaskCompleted(SignAUPTask t, VOMSUser u);
    
    void setApproveUserRequestTaskCompleted(ApproveUserRequestTask t, VOMSAdmin a);
    
    List <Task> findSignAUPTasks();
    
    List <Task> findApproveUserRequestTasks();
    
    void removeAllTasks();
    
    void deleteSignAUPTask(SignAUPTask t);
       
}

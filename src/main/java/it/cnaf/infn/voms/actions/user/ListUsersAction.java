package it.cnaf.infn.voms.actions.user;

import it.cnaf.infn.voms.dao.UserDAO;

import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.views.tiles.TilesResult;
import org.glite.security.voms.admin.model.VOMSUser;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Result(name="success", value="list-users.page", type=TilesResult.class)
@ParentPackage(value="base")
public class ListUsersAction extends ActionSupport  {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    List<VOMSUser> voMembers;
    
    
    @Override
    public String execute() throws Exception {
        voMembers  = UserDAO.instance().findAll();
        return SUCCESS;
    }
    
    

}

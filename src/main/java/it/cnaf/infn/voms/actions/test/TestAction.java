package it.cnaf.infn.voms.actions.test;


import org.apache.struts2.config.Result;
import org.apache.struts2.views.tiles.TilesResult;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;


@Result(name="success", value="Test-success.page", type=TilesResult.class)

public class TestAction extends ActionSupport implements ModelDriven <TestModel>{
    
    private static final long serialVersionUID = 1L;
    private TestModel model = new TestModel();
    
    
    @Override
    public String execute() throws Exception {
    
        model.setText( "Hello!" );
        return SUCCESS;
    }


    public TestModel getModel() {

        return model;
    }
}

package org.glite.security.voms.admin.view.actions.test;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.glite.security.voms.admin.view.actions.BaseAction;

import com.opensymphony.xwork2.ModelDriven;

@ParentPackage(value="base")
@Result(name="success", location="test.page", type="tiles")

public class TestAction extends BaseAction implements ModelDriven <TestModel>{
    
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

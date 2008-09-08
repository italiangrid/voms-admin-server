package org.glite.security.voms.admin.model;


public class Task {
    
    Long id;
    
    String name;
    
    String description;
    
    String className;
    
    
    public Task() {

        // TODO Auto-generated constructor stub
    }


    
    public String getClassName() {
    
        return className;
    }


    
    public void setClassName( String className ) {
    
        this.className = className;
    }


    
    public String getDescription() {
    
        return description;
    }


    
    public void setDescription( String description ) {
    
        this.description = description;
    }


    
    public Long getId() {
    
        return id;
    }


    
    public void setId( Long id ) {
    
        this.id = id;
    }


    
    public String getName() {
    
        return name;
    }


    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    

}

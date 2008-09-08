/*******************************************************************************
 *Copyright (c) Members of the EGEE Collaboration. 2006. 
 *See http://www.eu-egee.org/partners/ for details on the copyright
 *holders.  
 *
 *Licensed under the Apache License, Version 2.0 (the "License"); 
 *you may not use this file except in compliance with the License. 
 *You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 *Unless required by applicable law or agreed to in writing, software 
 *distributed under the License is distributed on an "AS IS" BASIS, 
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 *See the License for the specific language governing permissions and 
 *limitations under the License.
 *
 * Contributors:
 *     Andrea Ceccanti - andrea.ceccanti@cnaf.infn.it
 *******************************************************************************/
package org.glite.security.voms.admin.actionforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionMapping;



public class AddACLEntryActionForm extends ACLActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    Long userId;
    Long groupId;
    Long roleGroupId;
    Long roleId;
    Long tagId;
    
    String dn;
    Short caId;
    
    String entryKind;
    
    public AddACLEntryActionForm(){
        super();
    }
    
    
    public Short getCaId() {
    
        return caId;
    }
    
    public void setCaId( Short caId ) {
    
        this.caId = caId;
    }
    
    public String getDn() {
    
        return dn;
    }
    
    public void setDn( String dn ) {
    
        this.dn = dn;
    }
    
    public Long getGroupId() {
    
        return groupId;
    }
    
    public void setGroupId( Long groupId ) {
    
        this.groupId = groupId;
    }
    
    public Long getRoleId() {
    
        return roleId;
    }
    
    public void setRoleId( Long roleId ) {
    
        this.roleId = roleId;
    }
    
    public Long getUserId() {
    
        return userId;
    }
    
    public void setUserId( Long userId ) {
    
        this.userId = userId;
    }

    
    
    public String getEntryKind() {
    
        return entryKind;
    }


    
    public void setEntryKind( String entryKind ) {
    
        this.entryKind = entryKind;
    }


    public String toString() {
    
        return ToStringBuilder.reflectionToString(this);
        
    }
    
    
    
    
    public Long getRoleGroupId() {
    
        return roleGroupId;
    }


    
    public void setRoleGroupId( Long roleGroupId ) {
    
        this.roleGroupId = roleGroupId;
    }


    
    
    public Long getTagId() {
    
        return tagId;
    }


    
    public void setTagId( Long tagId ) {
    
        this.tagId = tagId;
    }


    public void reset( ActionMapping mapping, HttpServletRequest request ) {
        super.reset( mapping, request );
        userId = null;
        groupId = null;
        roleGroupId = null;
        roleId = null;
        tagId = null;
        dn = null;
        caId = null;
        entryKind = null;
        propagated = false;
        page = 0;
        
    }
    
}

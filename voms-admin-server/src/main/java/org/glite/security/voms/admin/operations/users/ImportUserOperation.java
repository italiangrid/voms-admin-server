/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glite.security.voms.admin.operations.users;

import java.util.concurrent.Callable;

import org.glite.security.voms.admin.apiv2.AUPAcceptanceRecordJSON;
import org.glite.security.voms.admin.apiv2.AttributeJSON;
import org.glite.security.voms.admin.apiv2.CertificateJSON;
import org.glite.security.voms.admin.apiv2.VOMSUserJSON;
import org.glite.security.voms.admin.operations.VOAdminOperation;
import org.glite.security.voms.admin.operations.VOMSContext;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.dao.VOMSAttributeDAO;
import org.glite.security.voms.admin.persistence.dao.VOMSCADAO;
import org.glite.security.voms.admin.persistence.dao.VOMSUserDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPDAO;
import org.glite.security.voms.admin.persistence.dao.generic.AUPVersionDAO;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskTypeDAO;
import org.glite.security.voms.admin.persistence.dao.hibernate.HibernateDAOFactory;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.AUPAcceptanceRecord;
import org.glite.security.voms.admin.persistence.model.AUPVersion;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.attribute.VOMSUserAttribute;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.glite.security.voms.admin.persistence.model.task.TaskType;

public class ImportUserOperation implements Callable<VOMSUser> {

  private VOMSUserJSON userJson;

  private ImportUserOperation(VOMSUserJSON userJson) {
    this.userJson = userJson;
  }

  @Override
  public VOMSUser call() throws Exception {
    VOMSUserDAO dao = VOMSUserDAO.instance();
    VOMSCADAO caDao = VOMSCADAO.instance();
    AUPDAO aupDAO = HibernateDAOFactory.instance().getAUPDAO();
    AUPVersionDAO aupVersionDao = DAOFactory.instance().getAUPVersionDAO();
    VOMSAttributeDAO attributeDao = VOMSAttributeDAO.instance();
    TaskTypeDAO ttDAO = DAOFactory.instance(DAOFactory.HIBERNATE)
        .getTaskTypeDAO();
    
    AUP voAup = aupDAO.getVOAUP();
    TaskType signAupTaskType = ttDAO.findByName("SignAUPTask");
    
    VOMSUser user = VOMSUser.fromVOMSUserJSON(userJson);
    
    user.setCreationTime(userJson.getCreationTime());
    user.setEndTime(userJson.getEndTime());
    user.setSuspended(userJson.getSuspended());
    user.setSuspensionReason(userJson.getSuspensionReason());
    user.setSuspensionReasonCode(userJson.getSuspensionReasonCode());
    
    HibernateFactory.getSession().saveOrUpdate(user);
    
    for (String fqan: userJson.getFqans()) {
      VOMSContext ctxt = VOMSContext.instance(fqan);
      
      if (ctxt.isGroupContext()){
        user.addToGroup(ctxt.getGroup());
      }else {
        user.assignRole(ctxt.getGroup(), ctxt.getRole());
      }
    }
    
    for (CertificateJSON c: userJson.getCertificates()){
      VOMSCA ca = caDao.getByName(c.getIssuerString());
      Certificate cert = new Certificate();
      cert.setSubjectString(c.getSubjectString());
      cert.setCa(ca);
      cert.setUser(user);
      cert.setSuspended(c.getSuspended());
      cert.setSuspensionReason(c.getSuspensionReason());
      cert.setCreationTime(c.getCreationTime());
      HibernateFactory.getSession().saveOrUpdate(cert);
      user.getCertificates().add(cert);
    }
    
    for (AUPAcceptanceRecordJSON ar: userJson.getAupAcceptanceRecords()){
      AUPVersion version = aupVersionDao.findByVersion(ar.getAupVersion());
      AUPAcceptanceRecord record = new AUPAcceptanceRecord();
      record.setValid(ar.isValid());
      record.setAupVersion(version);
      record.setLastAcceptanceDate(ar.getLastAcceptanceDate());  
      user.getAupAcceptanceRecords().add(record);
      record.setUser(user);
    }
    
    for (AttributeJSON attr: userJson.getAttributes()){
     VOMSUserAttribute userAttr = new VOMSUserAttribute(); 
     userAttr.setAttributeDescription(attributeDao.getAttributeDescriptionByName(attr.getName()));
     userAttr.setValue(attr.getValue());
     userAttr.setUser(user);
     user.getAttributes().add(userAttr);
    }
    
    if (userJson.getPendingSignAUPTask() != null){
      SignAUPTask task = new SignAUPTask();
      task.setAup(voAup);
      task.setCreationDate(userJson.getPendingSignAUPTask().getCreationDate());
      task.setExpiryDate(userJson.getPendingSignAUPTask().getExpirationDate());
      task.setStatus(TaskStatus.CREATED); 
      task.setUser(user);
      task.setType(signAupTaskType);
      user.getTasks().add(task);
    }
    
    HibernateFactory.getSession().save(user);
    

    return user;
  }

  public static VOAdminOperation<VOMSUser> instance(VOMSUserJSON user) {
    ImportUserOperation op = new ImportUserOperation(user);
    return new VOAdminOperation<>(op);
  }


}

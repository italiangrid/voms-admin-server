/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2006-2015
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
package notification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.glite.security.voms.admin.core.tasks.SignAUPReminderCheckTask;
import org.glite.security.voms.admin.persistence.dao.generic.DAOFactory;
import org.glite.security.voms.admin.persistence.dao.generic.TaskDAO;
import org.glite.security.voms.admin.persistence.model.AUP;
import org.glite.security.voms.admin.persistence.model.Certificate;
import org.glite.security.voms.admin.persistence.model.VOMSCA;
import org.glite.security.voms.admin.persistence.model.VOMSUser;
import org.glite.security.voms.admin.persistence.model.task.SignAUPTask;
import org.glite.security.voms.admin.persistence.model.task.Task.TaskStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AUPReminderTest {

  VOMSUser mockUser;
  AUP mockAUP;

  protected SignAUPTask buildTask(long now, int expirationDays) {

    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(now);

    cal.add(Calendar.DATE, expirationDays);

    SignAUPTask t = new SignAUPTask();
    t.setId(1L);
    t.setStatus(TaskStatus.CREATED);
    t.setExpiryDate(cal.getTime());
    t.setUser(mockUser);
    t.setAup(mockAUP);

    return t;
  }

  protected TaskDAO buildMockTaskDAO(SignAUPTask... aupTasks) {

    TaskDAO dao = mock(TaskDAO.class);

    when(dao.findActiveSignAUPTasks()).thenReturn(Arrays.asList(aupTasks));
    return dao;
  }

  protected DAOFactory buildMockDAOFactory(SignAUPTask... aupTasks) {

    TaskDAO dao = buildMockTaskDAO(aupTasks);

    DAOFactory daoFactory = mock(DAOFactory.class);
    when(daoFactory.getTaskDAO()).thenReturn(dao);

    return daoFactory;
  }

  @Before
  public void setup() {

    // Setup mock user
    mockUser = new VOMSUser();
    mockUser.setId(1L);
    mockUser.setName("Mock");
    mockUser.setSurname("User");

    VOMSCA mockCA = new VOMSCA();
    mockCA.setId((short) 1);
    mockCA.setSubjectString("CN=Mock CA");

    Certificate mockUserCert = new Certificate();
    mockUserCert.setId(1L);
    mockUserCert.setSubjectString("CN=Mock User");
    mockUserCert.setCa(mockCA);

    mockUser.addCertificate(mockUserCert);

    // Setup mock AUP
    mockAUP = new AUP();
    mockAUP.setName(AUP.VO_AUP_NAME);
    mockAUP.setReacceptancePeriod(365);
  }

  @Test
  public void testNoReminderIfFirstNotificationNotSent() {

    long now = System.currentTimeMillis();

    SignAUPTask t = buildTask(now, 15);
    
    MockTimeProvider tp = new MockTimeProvider(now);
    
    RecordingEventDispatcher ed = new RecordingEventDispatcher(tp);
    SignAUPReminderCheckTask task = new SignAUPReminderCheckTask(
      buildMockDAOFactory(t), ed, tp,
      Arrays.asList(7, 1), TimeUnit.DAYS);

    task.run();
    Assert.assertEquals(0,ed.getRecordedEvents().size());
  }

  @Test
  public void testNoExpectedReminderAfterFirstNotification() {

    long now = System.currentTimeMillis();

    SignAUPTask t = buildTask(now, 15);

    MockTimeProvider tp = new MockTimeProvider(now);
    RecordingEventDispatcher ed = new RecordingEventDispatcher(tp);

    SignAUPReminderCheckTask task = new SignAUPReminderCheckTask(
      buildMockDAOFactory(t), ed, tp, Arrays.asList(7, 1), TimeUnit.DAYS);
    
    // First notification was sent
    t.setLastNotificationTime(new Date(now));

    tp.add(TimeUnit.DAYS,5);
    task.run();

    Assert.assertEquals(0,ed.getRecordedEvents().size());
  }

  @Test
  public void testExpectedReminder() {

    long now = System.currentTimeMillis();

    // Build task that expires in 15 days from now
    SignAUPTask t = buildTask(now, 15);

    // Remind 10,5 and 1 days before expiration
    List<Integer> reminders = Arrays.asList(10, 5, 1);
    
    MockTimeProvider tp = new MockTimeProvider(now);
    RecordingEventDispatcher ed = new RecordingEventDispatcher(tp);
    SignAUPReminderCheckTask task = new SignAUPReminderCheckTask(
      buildMockDAOFactory(t), ed, tp, reminders, TimeUnit.DAYS);
    
    // Assume first notification was sent
    t.setLastNotificationTime(new Date(now));

    for (int i=0; i < 16; i++){
      tp.add(TimeUnit.DAYS,1);
      task.run();
    }
    
     
    Assert.assertEquals(3,ed.getRecordedEvents().size());
  
  }
  
  @Test
  public void testReminderEqualsPeriod() {

    long now = System.currentTimeMillis();

    // Build task that expires in 15 days from now
    SignAUPTask t = buildTask(now, 15);
    
    // Assume first notification was sent
    t.setLastNotificationTime(new Date(now));
    
    List<Integer> reminders = Arrays.asList(15);
    
    MockTimeProvider tp = new MockTimeProvider(now);
    RecordingEventDispatcher ed = new RecordingEventDispatcher(tp);
    SignAUPReminderCheckTask task = new SignAUPReminderCheckTask(
      buildMockDAOFactory(t), ed, tp, reminders, TimeUnit.DAYS);
    
    for (int i=0; i < 16; i++){
      tp.add(TimeUnit.DAYS,1);
      task.run();
    }
    
    Assert.assertEquals(0,ed.getRecordedEvents().size());
  
  }
}

/**
 * Copyright (c) Members of the EGEE Collaboration. 2006-2009.
 * See http://www.eu-egee.org/partners/ for details on the copyright holders.
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
 *
 * Authors:
 * 	Andrea Ceccanti (INFN)
 */
package org.glite.security.voms.admin.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.glite.security.voms.admin.operations.CurrentAdmin;
import org.glite.security.voms.admin.persistence.HibernateFactory;
import org.glite.security.voms.admin.persistence.model.VOMembershipRequest;
import org.hibernate.Query;

public class RequestDAO {

	private RequestDAO() {

		HibernateFactory.beginTransaction();

	}

	public static RequestDAO instance() {

		return new RequestDAO();
	}

	public VOMembershipRequest findFromAdmin() {
		CurrentAdmin admin = CurrentAdmin.instance();

		VOMembershipRequest req = findByDNCA(admin.getRealSubject(), admin
				.getRealIssuer());

		return req;
	}

	public VOMembershipRequest createFromDNCA(String dn, String ca,
			String emailAddress) {

		VOMembershipRequest req = new VOMembershipRequest();

		req.setDn(dn);
		req.setCa(ca);
		req.setCn(null);
		req.setEmailAddress(emailAddress);

		req.setCreationDate(new Date());
		req.setStatus(VOMembershipRequest.SUBMITTED);

		req.setConfirmId(UUID.randomUUID().toString());
		save(req);

		return req;

	}

	public VOMembershipRequest createFromAdmin(String emailAddress) {

		CurrentAdmin admin = CurrentAdmin.instance();

		VOMembershipRequest req = new VOMembershipRequest();

		req.setDn(admin.getRealSubject());
		req.setCa(admin.getRealIssuer());
		req.setCn(admin.getRealCN());
		req.setEmailAddress(emailAddress);
		req.setCreationDate(new Date());
		req.setStatus(VOMembershipRequest.SUBMITTED);

		req.setConfirmId(UUID.randomUUID().toString());
		save(req);

		return req;
	}

	public VOMembershipRequest findById(Long id) {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where id = :id";
		Query q = HibernateFactory.getSession().createQuery(query).setLong(
				"id", id.longValue());

		return (VOMembershipRequest) q.uniqueResult();
	}

	public VOMembershipRequest findPendingForUser(String dn, String caDN) {
		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where dn = :dn and ca = :ca and status <= 1";
		Query q = HibernateFactory.getSession().createQuery(query).setString(
				"dn", dn).setString("ca", caDN);

		return (VOMembershipRequest) q.uniqueResult();

	}

	public VOMembershipRequest findByDNCA(String dn, String caDN) {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where dn = :dn and ca = :ca and status = 0";
		Query q = HibernateFactory.getSession().createQuery(query).setString(
				"dn", dn).setString("ca", caDN);

		return (VOMembershipRequest) q.uniqueResult();
	}

	public List getUnconfirmed() {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where status = 0";
		Query q = HibernateFactory.getSession().createQuery(query);

		return q.list();

	}

	public List getPending() {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where status = :status";

		Query q = HibernateFactory.getSession().createQuery(query).setInteger(
				"status", VOMembershipRequest.CONFIRMED.intValue());

		return q.list();

	}

	public List getProcessed() {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest where status = :approved or status = :rejected";

		Query q = HibernateFactory
				.getSession()
				.createQuery(query)
				.setInteger("approved", VOMembershipRequest.APPROVED.intValue())
				.setInteger("rejected", VOMembershipRequest.REJECTED.intValue());

		return q.list();

	}

	public List getAll() {

		String query = "from org.glite.security.voms.admin.persistence.model.VOMembershipRequest";

		return HibernateFactory.getSession().createQuery(query).list();

	}

	public void save(VOMembershipRequest req) {

		HibernateFactory.getSession().save(req);

	}

	public void update(VOMembershipRequest req) {

		HibernateFactory.getSession().update(req);

	}

	public void delete(VOMembershipRequest req) {

		HibernateFactory.getSession().delete(req);
	}

}

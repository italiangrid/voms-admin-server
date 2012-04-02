package org.glite.security.voms.admin.notification.messages;

import java.util.List;

import org.glite.security.voms.admin.configuration.VOMSConfiguration;
import org.glite.security.voms.admin.persistence.model.VOMSUser;

public class ExpiredMembersWarning extends AbstractVelocityNotification {
	
	List<VOMSUser> expiredMembers;
	
	public ExpiredMembersWarning(List<VOMSUser> expiredMembers) {
		
		this.expiredMembers = expiredMembers;
	}
	
	@Override
	protected void buildMessage() {
		
		VOMSConfiguration conf = VOMSConfiguration.instance();
		String voName = conf.getVOName();

		setSubject(String.format("Expired members notice for VO %s.", voName));
		
		context.put("voName", voName);
		context.put("expiredMembers", expiredMembers);
		
		super.buildMessage();
	}

}

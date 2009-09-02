package org.glite.security.voms.admin.event;

public enum EventType {

	UserMembershipEvent(0), UserAUPEvent(1), UserSuspensionEvent(2), VOMembershipRequestEvent(
			3), GroupMembershipRequestEvent(4), RoleMembershipRequestEvent(5);

	int bitNo;

	private EventType(int b) {
		bitNo = b;
	}

	public int getBitNo() {
		return bitNo;
	}

}

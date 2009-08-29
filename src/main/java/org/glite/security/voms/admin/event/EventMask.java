package org.glite.security.voms.admin.event;

import java.util.BitSet;
import java.util.StringTokenizer;

public final class EventMask extends BitSet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventMask(EventType... eventTypes) {

		for (EventType t : eventTypes)
			set(t.bitNo);
	}

	public EventMask(String types) {
		setTypes(types);
	}

	public void setTypes(String types) {

		if (types == null)
			return;

		StringTokenizer st = new StringTokenizer(types, ",");
		while (st.hasMoreTokens()) {

			String type = st.nextToken();

			int bitNo = Integer.valueOf(type);

			set(bitNo);
		}
	}

}

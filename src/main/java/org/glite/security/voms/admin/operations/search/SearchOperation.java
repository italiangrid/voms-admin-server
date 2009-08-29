package org.glite.security.voms.admin.operations.search;

import org.glite.security.voms.admin.operations.VOMSOperation;
import org.glite.security.voms.admin.view.actions.SearchData;

public interface SearchOperation extends VOMSOperation {

	SearchData getSearchData();

	void setSearchData(SearchData data);

}

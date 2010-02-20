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
package org.glite.security.voms.admin.taglib;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.glite.security.voms.admin.persistence.dao.SearchResults;

public class JavaScriptSearchNavBarTag extends SearchNavBarTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String searchPanelId;

	@Override
	protected void writeLink(SearchResults res, int firstResult, String content)
			throws JspException, IOException {

		StringBuilder link = new StringBuilder();
		String url;

		try {
			url = buildURL(res.getSearchString(), firstResult);

		} catch (MalformedURLException e) {

			throw new JspTagException("Error building searchURL: "
					+ e.getMessage(), e);

		}

		String jquerySearch = String.format("javascript:ajaxLoad('%s','%s');",
				getSearchPanelId(), url);
		link.append("<a href=\"" + jquerySearch + "\" class=\""
				+ linkStyleClass + "\">" + content + "</a>");

		pageContext.getOut().write(link.toString());

	}

	public String getSearchPanelId() {
		return searchPanelId;
	}

	public void setSearchPanelId(String searchPanelId) {
		this.searchPanelId = searchPanelId;
	}

}

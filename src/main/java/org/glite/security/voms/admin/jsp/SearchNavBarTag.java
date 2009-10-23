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
package org.glite.security.voms.admin.jsp;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.dao.SearchResults;

public class SearchNavBarTag extends TagSupport {

	private static final Log log = LogFactory.getLog(SearchNavBarTag.class);

	String id;

	String searchURL;

	String context;

	String permission;

	String styleClass;

	String disabledLinkStyleClass;

	String linkStyleClass;

	String searchType;

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public SearchNavBarTag() {

		super();

	}

	protected String buildURL(String text, int firstResult)
			throws MalformedURLException {

		StringBuilder url = new StringBuilder(searchURL);

		String querySeparator = "?";

		if (url.indexOf(querySeparator) != -1)
			querySeparator = "&";

		url.append(querySeparator + "searchData.firstResult=" + firstResult);

		if (text != null)
			url.append("&searchData.text=" + text);

		if (getSearchType() != null)
			url.append("&searchData.type=" + getSearchType());

		return url.toString();
	}

	protected void writeLink(SearchResults res, int firstResult, String content)
			throws JspException, IOException {

		String link;
		String url;

		try {
			url = buildURL(res.getSearchString(), firstResult);

		} catch (MalformedURLException e) {

			throw new JspTagException("Error building searchURL: "
					+ e.getMessage(), e);

		}

		if (org.glite.security.voms.admin.jsp.TagUtils.isAuthorized(
				pageContext, context, permission)) {

			link = "<a href=\"" + url + "\" class=\"" + linkStyleClass + "\">"
					+ content + "</a>";

		} else
			link = "<div class=\"" + disabledLinkStyleClass + "\">" + content
					+ "</div>";

		pageContext.getOut().write(link);

	}

	protected void writeFirst(SearchResults res) throws JspException,
			IOException {

		writeLink(res, 0, "first");

	}

	protected void writeLast(SearchResults res) throws JspException,
			IOException {

		writeLink(res, res.getCount() - res.getResultsPerPage(), "last");

	}

	protected void writePrevious(SearchResults res) throws JspException,
			IOException {

		int previousIndex = res.getFirstResult() - res.getResultsPerPage();
		if (previousIndex < 0)
			previousIndex = 0;

		writeLink(res, previousIndex, "&lt;");

	}

	protected void writeNext(SearchResults res) throws JspException,
			IOException {

		writeLink(res, res.getFirstResult() + res.getResultsPerPage(), "&gt;");

	}

	protected void writeResultCount(SearchResults res) throws JspException,
			IOException {

		String resCount = (res.getFirstResult() + 1) + "-"
				+ (res.getFirstResult() + res.getResults().size()) + " of "
				+ res.getCount();

		pageContext.getOut().write(resCount);

	}

	public int doStartTag() throws JspException {

		SearchResults results = (SearchResults) pageContext.findAttribute(id);

		if (results == null) {

			// Don't write anything...
			return SKIP_BODY;
		}
		try {

			pageContext.getOut().write("<div class=\"" + styleClass + "\">\n");

			if (results.getFirstResult() > 0) {

				// Not the second page
				if ((results.getFirstResult() - results.getResultsPerPage()) > 0)
					writeFirst(results);

				writePrevious(results);

			}

			writeResultCount(results);

			if (results.getRemainingCount() > 0) {

				writeNext(results);

				// Not the last - 1 page.
				if (results.getRemainingCount() > results.getResultsPerPage()) {

					writeLast(results);
				}
			}

			pageContext.getOut().write("</div>");
		} catch (IOException e) {

			log.error("Error writing jsp page: " + e.getMessage(), e);
			throw new JspException("Error writing jsp page: " + e.getMessage(),
					e);
		}
		return SKIP_BODY;
	}

	public String getContext() {

		return context;
	}

	public void setContext(String context) {

		this.context = context;
	}

	public String getDisabledLinkStyleClass() {

		return disabledLinkStyleClass;
	}

	public void setDisabledLinkStyleClass(String disabledLinkStyleClass) {

		this.disabledLinkStyleClass = disabledLinkStyleClass;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getLinkStyleClass() {

		return linkStyleClass;
	}

	public void setLinkStyleClass(String linkStyleClass) {

		this.linkStyleClass = linkStyleClass;
	}

	public String getPermission() {

		return permission;
	}

	public void setPermission(String permission) {

		this.permission = permission;
	}

	public String getSearchURL() {

		return searchURL;
	}

	public void setSearchURL(String searchURL) {

		this.searchURL = searchURL;
	}

	public String getStyleClass() {

		return styleClass;
	}

	public void setStyleClass(String styleClass) {

		this.styleClass = styleClass;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
}

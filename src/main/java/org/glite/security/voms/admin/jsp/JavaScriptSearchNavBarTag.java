package org.glite.security.voms.admin.jsp;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.glite.security.voms.admin.dao.SearchResults;

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

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
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.DOM2Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.core.VOMSServiceConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PanelTag extends TagSupport {

	private static final Log log = LogFactory.getLog(PanelTag.class);

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	String id;
	String title;

	String headerClass;
	String contentClass;
	String panelClass;
	String titleClass;
	String buttonClass;

	protected boolean isActive() {
		Map statusMap = (Map) pageContext
				.findAttribute(VOMSServiceConstants.STATUS_MAP_KEY);

		log.debug("statusMap:" + statusMap);
		if (statusMap == null)
			return true;

		Boolean status = (Boolean) statusMap.get(getId());

		if (status == null)
			return true;

		return status.booleanValue();

	}

	protected String getUrl() {

		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

		return req.getContextPath() + "/UI.do";

	}

	protected String getGetStatusJs() {

		return "getStatus('" + getUrl() + "','" + getId() + "','" + getImgUrl()
				+ "')";
	}

	protected String getShowJs() {
		if (isActive())
			return "show('" + getUrl() + "','" + getContentId() + "',this,'"
					+ getImgUrl() + "')";
		else
			return "hide('" + getUrl() + "','" + getContentId() + "',this,'"
					+ getImgUrl() + "')";
	}

	protected String getJs() {

		return "toggleVisibility('" + getUrl() + "', '" + getId() + "','"
				+ getImgUrl() + "')";
	}

	protected String getLoadJs() {

		return "load('" + getId() + "','" + getImgUrl() + "')";
	}

	protected String getImgUrl() {

		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

		return req.getContextPath() + "/img";

	}

	protected String getImgSrc() {
		if (isActive())
			return getImgUrl() + "/minimize.png";
		else
			return getImgUrl() + "/maximize.png";

	}

	protected String getHeaderId() {

		return id + "_header";
	}

	protected String getContentId() {

		return id + "_content";
	}

	protected String getButtonId() {

		return id + "_button";
	}

	protected String getReloadId() {

		return id + "_reload";
	}

	protected String getTitleId() {

		return id + "_title";
	}

	protected void setCookie() {

		Cookie c = new Cookie(getId(), Boolean.toString(isActive()));
		c.setMaxAge(60 * 60);
		HttpServletResponse res = (HttpServletResponse) pageContext
				.getResponse();
		res.addCookie(c);

	}

	public PanelTag() {

		super();
		// TODO Auto-generated constructor stub
	}

	protected void buildHeaderContent(Document doc) {

		// Build title element
		Element titleElement = doc.createElement("div");
		titleElement.setAttribute("id", getTitleId());
		titleElement.setAttribute("class", (titleClass == null ? ""
				: titleClass));
		titleElement.appendChild(doc.createTextNode(getTitle()));

		doc.getDocumentElement().appendChild(titleElement);

		// Build button element
		Element buttonElement = doc.createElement("img");
		buttonElement.setAttribute("id", getButtonId());
		buttonElement.setAttribute("class", (buttonClass == null ? ""
				: buttonClass));
		buttonElement.setAttribute("src", getImgSrc());
		// buttonElement.setAttribute("onclick",getJs());
		buttonElement.setAttribute("style", "cursor:pointer;");
		buttonElement.setAttribute("alt", "toggle");

		doc.getDocumentElement().appendChild(buttonElement);

	}

	public void write() throws JspException {

		DocumentBuilder builder = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);

		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {

			log.error("Error creating XML parser!", e1);

			throw new JspTagException("Error creating XML parser!", e1);

		} catch (FactoryConfigurationError e1) {
			log.error("Error creating XML parser!", e1);

			throw new JspTagException("Error creating XML parser!", e1);

		}

		Document doc = builder.getDOMImplementation().createDocument(
				"http://c", "div", null);

		try {
			// Write panel element
			pageContext.getOut().write(
					"<div id=\"" + getId() + "\" class='" + getPanelClass()
							+ "' >");

			// Use dom to build quite complicated (and self contained) header
			// element
			// (don't want to get mad escaping all those attributes!)

			doc.getDocumentElement().setAttribute("id", getHeaderId());
			doc.getDocumentElement().setAttribute("class", getHeaderClass());

			buildHeaderContent(doc);

			doc.normalize();

			String headerString = DOM2Writer.nodeToString(doc, true);

			log.debug("Header XML: " + headerString);

			// Write header
			pageContext.getOut().write(headerString);

			// Write content
			// 

			if (isActive())
				pageContext.getOut().write(
						"<div id=\""
								+ getContentId()
								+ "\" class='"
								+ (getContentClass() == null ? ""
										: getContentClass())
								+ "' style='clear:both'>");
			else
				pageContext.getOut().write(
						"<div id=\""
								+ getContentId()
								+ "\" class='"
								+ (getContentClass() == null ? ""
										: getContentClass())
								+ "' style='clear: both; display: none'>");

			pageContext.getOut().write("<div class='separator'>&nbsp;</div>");
			pageContext.getOut().write("<div>");
		} catch (IOException e) {

			log.error("Error writing jsp page: " + e.getMessage(), e);
			throw new JspException("Error writing jsp page: " + e.getMessage(),
					e);
		}
	}

	public int doStartTag() throws JspException {

		write();

		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {

		try {
			pageContext.getOut().write("</div>");
			pageContext.getOut().write(
					"</div><!-- " + getContentId() + " --> </div> <!-- "
							+ getId() + " -->");
			pageContext.getOut().write("<div class='separator'>&nbsp;</div>");
		} catch (IOException e) {

			log.error("Error writing jsp page: " + e.getMessage(), e);
			throw new JspException("Error writing jsp page: " + e.getMessage(),
					e);
		}
		return EVAL_PAGE;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getTitle() {

		return title;
	}

	public void setTitle(String title) {

		this.title = title;
	}

	public String getContentClass() {
		return contentClass;
	}

	public void setContentClass(String contentClass) {
		this.contentClass = contentClass;
	}

	public String getHeaderClass() {
		return headerClass;
	}

	public void setHeaderClass(String headerClass) {
		this.headerClass = headerClass;
	}

	public String getPanelClass() {
		return panelClass;
	}

	public void setPanelClass(String panelClass) {
		this.panelClass = panelClass;
	}

	public String getButtonClass() {
		return buttonClass;
	}

	public void setButtonClass(String buttonClass) {
		this.buttonClass = buttonClass;
	}

	public String getTitleClass() {
		return titleClass;
	}

	public void setTitleClass(String titleClass) {
		this.titleClass = titleClass;
	}

}

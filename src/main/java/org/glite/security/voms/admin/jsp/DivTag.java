package org.glite.security.voms.admin.jsp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.glite.security.voms.admin.common.VOMSServiceConstants;

public class DivTag extends TagSupport {

	String id;

	String cssClass;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	protected boolean isVisible() {
		Map statusMap = (Map) pageContext
				.findAttribute(VOMSServiceConstants.STATUS_MAP_KEY);

		if (statusMap == null)
			return true;

		Boolean status = (Boolean) statusMap.get(getId());

		if (status == null)
			return true;

		return status;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	@Override
	public int doStartTag() throws JspException {

		String styleContent = "";

		if (!isVisible())
			styleContent = "display:none";

		String startTag = String.format(
				"<div id=\"%s\" class=\"%s\" style=\"%s\">", getId(),
				getCssClass(), styleContent);

		try {
			pageContext.getOut().write(startTag);

		} catch (IOException e) {
			throw new JspTagException("Error writing to jsp writer!", e);
		}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().write("\n</div><!-- " + getId() + "-->");

		} catch (IOException e) {
			throw new JspTagException("Error writing to jsp writer!", e);
		}

		return EVAL_PAGE;
	}

}

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
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.core.LoopTagSupport;
import javax.servlet.jsp.tagext.TagSupport;

import ognl.OgnlContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.security.voms.admin.operations.VOMSPermission;

public class PrintPermissionTag extends TagSupport {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public static final String SHORT_HEADER = "short";

	public static final String LONG_HEADER = "long";

	private static final Log log = LogFactory.getLog(PrintPermissionTag.class);

	String header;

	String var;

	private static final String[] headerNames = { "Container", "Membership",
			"ACL", "Attributes", "Requests", "Suspend" };

	private static final String[] shortHeaderNames = { "Cont.", "Memb.", "ACL",
			"Attr.", "Req." };

	public String getHeader() {

		return header;
	}

	public void setHeader(String header) {

		this.header = header;
	}

	private void printLongHeader() throws JspException {

		JspWriter out = pageContext.getOut();

		try {

			out
					.println("<table class='table' cellpadding='0' cellspacing='0'>");
			out.println("<tr class='tableHeaderRow'>");

			for (int i = 0; i < headerNames.length; i++)
				out.println("<td>" + headerNames[i] + "</td>");

			out.println("</tr>");

		} catch (IOException e) {

			throw new JspTagException(e.getMessage());
		}

	}

	private void printShortHeader() throws JspException {

		JspWriter out = pageContext.getOut();

		try {

			out
					.println("<table class='table' cellpadding='0' cellspacing='0'>");
			out.println("<tr class='tableHeaderRow'>");

			for (int i = 0; i < shortHeaderNames.length; i++)
				out.println("<td>" + shortHeaderNames[i] + "</td>");

			out.println("</tr>");

		} catch (IOException e) {

			throw new JspTagException(e.getMessage());
		}

	}

	private void printPermissions(VOMSPermission p) throws JspException {

		JspWriter out = pageContext.getOut();

		try {

			// out.println( "<tr class='tableRow'>" );
			// CONTAINER permissions
			out.write("<td>");
			if (p.hasContainerReadPermission())
				out.write("r");
			if (p.hasContainerWritePermission())
				out.write("w");

			out.write("</td>");

			// MEMBERSHIP permissions
			out.write("<td>");
			if (p.hasMembershipReadPermission())
				out.write("r");
			if (p.hasMembershipWritePermission())
				out.write("w");
			out.write("</td>");

			// ACL permissions
			out.write("<td>");
			if (p.hasACLReadPermission())
				out.write("r");
			if (p.hasACLWritePermission())
				out.write("w");
			if (p.hasACLDefaultPermission())
				out.write("d");
			out.write("</td>");

			// ATTRIBUTES permissions
			out.write("<td>");
			if (p.hasAttributeReadPermission())
				out.write("r");
			if (p.hasAttributeWritePermission())
				out.write("w");
			out.write("</td>");

			// REQUESTS permissions
			out.write("<td>");
			if (p.hasRequestReadPermission())
				out.write("r");
			if (p.hasRequestWritePermission())
				out.write("w");
			out.write("</td>");

			// PERSONAL INFO
			out.write("<td>");
			if (p.hasPersonalInfoReadPermission())
				out.write("r");
			if (p.hasPersonalInfoWritePermission())
				out.write("w");
			out.write("</td>");

			// SUSPEND permission
			out.write("<td>");
			if (p.hasSuspendPermission())
				out.write("yes");
			out.write("</td>");

			// out.println( "</tr>" );
		} catch (IOException e) {

			throw new JspTagException(e.getMessage());

		}

	}

	private void printFooter() throws JspException {

		JspWriter out = pageContext.getOut();

		try {
			out.write("</table>");
		} catch (IOException e) {

			throw new JspTagException(e.getMessage());
		}

	}

	public int doStartTag() throws JspException {

		// Look in request context
		Object o = pageContext.getAttribute(getVar());

		VOMSPermission p;

		if (o instanceof Map.Entry) {
			p = (VOMSPermission) ((Map.Entry) o).getValue();

		} else
			p = (VOMSPermission) o;

		if (header == null)
			header = SHORT_HEADER;

		// if ( SHORT_HEADER.equals( header ) )
		// printShortHeader();

		// if ( LONG_HEADER.equals( header ) )
		// printLongHeader();

		printPermissions(p);

		// printFooter();

		return SKIP_BODY;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

}

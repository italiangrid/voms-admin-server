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

package org.glite.security.voms.admin.servlets.vomses;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glite.security.voms.admin.util.AdminServiceContactInfo;

public class RedirectServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String getVONameFromContextPath(String contextPath){
		
		// Strip first slash
		String path = contextPath.substring(1);
		
		int nextSlashIndex = path.indexOf('/');
		
		if (nextSlashIndex > 0)
			path = path.substring(0, nextSlashIndex);
		
		return path;
	}
	
	protected String getRedirectFromVOConfiguration(HttpServletRequest req){
		
		@SuppressWarnings("unchecked")
		List<AdminServiceContactInfo> endpoints = (List<AdminServiceContactInfo>) req.getServletContext().getAttribute(VOMSESContextListener.ENDPOINTS_KEY);
		
		if (req.getPathInfo() == null)
			return "/";
		
		String voName  = getVONameFromContextPath(req.getPathInfo());
		
		for ( AdminServiceContactInfo e: endpoints){
			
			if (e.getVoName().equals(voName)){
				StringBuilder url = new StringBuilder(String.format("%s%s", e.getBaseURL(),req.getRequestURI())); 
				
				if (req.getQueryString() != null)
					url.append("?"+req.getQueryString());
				
				return url.toString();	
			}
		}
		
		return null;
	}	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String url = getRedirectFromVOConfiguration(req);
		
		if (url != null)	
			resp.sendRedirect(url);
		
		super.doGet(req, resp);
	}

}

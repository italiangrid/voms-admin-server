package org.glite.security.voms.admin.servlets.vomses;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.glite.security.voms.admin.util.AdminServiceContactInfo;

public class VOMSStatusFilter implements Filter {

	public static final String STATUS_MAP_KEY = "statusMap";
	
	public VOMSStatusFilter() {
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		Map<String, Boolean> statusMap = new HashMap<String, Boolean>();
		
		@SuppressWarnings("unchecked")
		List<AdminServiceContactInfo> endpoints = (List<AdminServiceContactInfo>) request.getServletContext().getAttribute(VOMSESContextListener.ENDPOINTS_KEY);
		
		for (AdminServiceContactInfo e: endpoints){
			statusMap.put(e.getVoName(), StatusUtil.isActive(e.getVoName()));
		}
		
		request.setAttribute(STATUS_MAP_KEY, statusMap);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		

	}

}

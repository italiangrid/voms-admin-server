package org.italiangrid.voms.aa.x509.stats;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


public class ACServletStats implements Filter {

	public ACServletStats() {}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		
		long startTime = System.currentTimeMillis();
		
		chain.doFilter(request, response);
		
		long executionTime = System.currentTimeMillis() - startTime;
		
		ACEndpointStats.INSTANCE.addValue(executionTime);
		
	}

	@Override
	public void destroy() {}

}

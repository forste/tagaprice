package org.tagaprice.server;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * Initialization Servlet
 * 
 *  This Servlet simply does initialization stuff, not more, not less
 */
public class InitServlet extends GenericServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		/// TODO add initialization code here
	}
	
	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		// nothing
	}

}

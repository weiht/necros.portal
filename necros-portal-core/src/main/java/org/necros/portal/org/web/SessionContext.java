package org.necros.portal.org.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.necros.portal.org.Person;

public class SessionContext {
	private HttpServletRequest request;
	
	private SessionContext(HttpServletRequest req) {
		this.request = req;
	}
	
	public static SessionContext getCurrentContext(HttpServletRequest req) {
		return new SessionContext(req);
	}
	
	public Person getCurrentUser() {
		return getSessionValue(SessionContextKeys.CURRENT_USER);
	}

	@SuppressWarnings("unchecked")
	private <T> T getSessionValue(String currentUser) {
		if (request == null) return null;
		HttpSession session = request.getSession(false);
		if (session == null) return null;
		return (T) session.getAttribute(currentUser);
	}
}

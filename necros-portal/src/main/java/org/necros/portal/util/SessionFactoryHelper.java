/**
 * 
 */
package org.necros.portal.util;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author weiht
 *
 */
public class SessionFactoryHelper {
	private SessionFactory sessionFactory;
	
	public Criteria createCriteria(Class<?> clazz) {
		return sessionFactory.getCurrentSession().createCriteria(clazz);
	}
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}

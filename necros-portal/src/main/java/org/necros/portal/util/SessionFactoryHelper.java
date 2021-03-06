/**
 * 
 */
package org.necros.portal.util;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

/**
 * @author weiht
 *
 */
public class SessionFactoryHelper {
	private SessionFactory sessionFactory;
	
	public Criteria createCriteria(Class<?> clazz) {
		return sessionFactory.getCurrentSession().createCriteria(clazz);
	}
	
	public Criteria createCriteria(String entityName) {
		return sessionFactory.getCurrentSession().createCriteria(entityName);
	}

	public int count(Criteria c) {
		return ((Long)c.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("rawtypes")
	public List page(Criteria c, Pager p) {
		return c.setFirstResult(p.getQueryFirst())
				.setFetchSize(p.getPageSize())
				.setMaxResults(p.getPageSize())
				.list();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageQueryResult pageResult(Criteria c, Pager p) {
		return new PageQueryResult(p, page(c, p));
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

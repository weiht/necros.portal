/**
 * 
 */
package org.necros.portal.scripting.h4;

import java.io.IOException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.scripting.ScriptService;
import org.necros.portal.scripting.ServerSideScript;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class ScriptServiceH4 implements ScriptService {
	public static final Class<?> clazz = ServerSideScript.class;
	
	private SessionFactoryHelper sessionFactoryHelper;

	@Override
	public ServerSideScript get(String name) {
		return (ServerSideScript) sessionFactoryHelper.getSession().get(clazz, name);
	}

	@Override
	public ServerSideScript create(ServerSideScript s) {
		sessionFactoryHelper.getSession().save(s);
		return s;
	}

	@Override
	public ServerSideScript update(ServerSideScript s) {
		sessionFactoryHelper.getSession().update(s);
		return s;
	}

	@Override
	public ServerSideScript remove(String name) {
		ServerSideScript s = get(name);
		if (s != null) sessionFactoryHelper.getSession().delete(s);
		return s;
	}
	
	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerSideScript> all() {
		return createCriteria()
				.list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<ServerSideScript> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}
	
	private Criteria createFilter(String filterText) {
		return createCriteria().add(Restrictions.or(
				Restrictions.like("name", filterText, MatchMode.ANYWHERE),
				Restrictions.like("displayName", filterText, MatchMode.ANYWHERE),
				Restrictions.like("description", filterText, MatchMode.ANYWHERE)
				));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerSideScript> filter(String filterText) {
		return createFilter(filterText)
				.list();
	}

	@Override
	public int countFiltered(String filterText) {
		return sessionFactoryHelper.count(createFilter(filterText));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<ServerSideScript> pageFiltered(Pager p, String filterText) {
		p.setRecordCount(countFiltered(filterText));
		return sessionFactoryHelper.pageResult(createFilter(filterText), p);
	}

	@Override
	public String exportAll() throws IOException {
		return null;
	}

	@Override
	public void importAll(String fn) throws IOException {
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

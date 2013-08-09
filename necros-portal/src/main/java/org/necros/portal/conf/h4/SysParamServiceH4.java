/**
 * 
 */
package org.necros.portal.conf.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.conf.AbstractSysParamService;
import org.necros.portal.conf.SysParam;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class SysParamServiceH4 extends AbstractSysParamService {
	private SessionFactoryHelper sessionFactoryHelper;
	
	@Override
	public SysParam get(String key) {
		return (SysParam) sessionFactoryHelper.getSession().get(SysParam.class, key);
	}
	
	@Override
	public SysParam create(SysParam p) {
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	@Override
	public SysParam update(SysParam p) {
		sessionFactoryHelper.getSession().update(p);
		return p;
	}

	@Override
	public SysParam remove(String key) {
		SysParam p = get(key);
		if (p != null) sessionFactoryHelper.getSession().delete(p);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysParam> all() {
		return createCriteria().list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(SysParam.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<SysParam> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysParam> filter(String filter) {
		return createFilter(filter).list();
	}

	private Criteria createFilter(String filter) {
		return createCriteria()
				.add(Restrictions.or(Restrictions.like("key", filter, MatchMode.ANYWHERE),
						Restrictions.like("description", filter, MatchMode.ANYWHERE)));
	}

	@Override
	public int countFiltered(String filter) {
		return sessionFactoryHelper.count(createFilter(filter));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<SysParam> pageFiltered(Pager p, String filter) {
		p.setRecordCount(countFiltered(filter));
		return sessionFactoryHelper.pageResult(createFilter(filter), p);
	}

	@Override
	protected SysParam doFindParamByKey(String key) {
		return (SysParam) sessionFactoryHelper.getSession().get(SysParam.class, key);
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

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
import org.necros.portal.conf.CategoryService;
import org.necros.portal.conf.DictCategory;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class CategoryServiceH4 implements CategoryService {
	private SessionFactoryHelper sessionFactoryHelper;
	
	@Override
	public DictCategory get(String key) {
		return (DictCategory) sessionFactoryHelper.getSession().get(DictCategory.class, key);
	}
	
	@Override
	public DictCategory create(DictCategory p) {
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	@Override
	public DictCategory update(DictCategory p) {
		sessionFactoryHelper.getSession().update(p);
		return p;
	}

	@Override
	public DictCategory remove(String key) {
		DictCategory p = get(key);
		sessionFactoryHelper.getSession().delete(p);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictCategory> all() {
		return createCriteria().list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(DictCategory.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<DictCategory> pageAll(Pager p) {
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictCategory> filter(String filter) {
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
	public PageQueryResult<DictCategory> pageFiltered(Pager p, String filter) {
		return sessionFactoryHelper.pageResult(createFilter(filter), p);
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

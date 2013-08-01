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
import org.necros.portal.conf.DictEntry;
import org.necros.portal.conf.EntryService;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class EntryServiceH4 implements EntryService {
	private SessionFactoryHelper sessionFactoryHelper;
	private String categoryId;
	
	@Override
	public DictEntry get(String key) {
		return (DictEntry) createCriteria()
				.add(Restrictions.eq("key", key))
				.uniqueResult();
	}
	
	@Override
	public DictEntry create(DictEntry p) {
		p.setCategoryId(categoryId);
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	@Override
	public DictEntry update(DictEntry p) {
		p.setCategoryId(categoryId);
		sessionFactoryHelper.getSession().update(p);
		return p;
	}

	@Override
	public DictEntry remove(String key) {
		DictEntry p = get(key);
		sessionFactoryHelper.getSession().delete(p);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictEntry> all() {
		return createCriteria().list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(DictEntry.class)
				.add(Restrictions.eq("categoryId", categoryId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<DictEntry> pageAll(Pager p) {
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictEntry> filter(String filter) {
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
	public PageQueryResult<DictEntry> pageFiltered(Pager p, String filter) {
		return sessionFactoryHelper.pageResult(createFilter(filter), p);
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
}

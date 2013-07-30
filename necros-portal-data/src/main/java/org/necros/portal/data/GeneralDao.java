/**
 * 
 */
package org.necros.portal.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class GeneralDao {
	private SessionFactory sessionFactory;
	private IdGenerator idGenerator;
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected Criteria createCriteria(String entity) {
		return getSession().createCriteria(entity);
	}

	protected int count(Criteria c) {
		return ((Long)c.setProjection(Projections.count(GeneralProperties.PROP_ID))
				.uniqueResult()).intValue();
	}
	
	@SuppressWarnings("rawtypes")
	protected List page(Criteria c, Pager p) {
		return c.setFirstResult(p.getQueryFirst())
				.setFetchSize(p.getPageSize())
				.list();
	}
	
	@SuppressWarnings("rawtypes")
	public Map get(String entity, Serializable id) {
		return (Map) getSession().get(entity, id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map create(String entity, Map obj) {
		obj.put(GeneralProperties.PROP_ID, idGenerator.generate());
		getSession().save(entity, obj);
		return obj;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map update(String entity, Map obj) {
		Map orig = get(entity, (Serializable) obj.get(GeneralProperties.PROP_ID));
		//TODO 先提取，然后复制允许修改的属性。注意，有些属性可能被清空了。
		for (Object o: obj.entrySet()) {
			Entry e = (Entry)o;
			orig.put(e.getKey(), e.getValue());
		}
		getSession().save(orig);
		return orig;
	}
	
	@SuppressWarnings("rawtypes")
	public Map remove(String entity, Serializable id) {
		Map orig = get(entity, id);
		if (orig != null) {
			getSession().delete(entity, orig);
		}
		return orig;
	}
	
	@SuppressWarnings("rawtypes")
	public List all(String entity) {
		return createCriteria(entity).list();
	}
	
	public int countAll(String entity) {
		return count(createCriteria(entity));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageQueryResult pageAll(String entity, Pager p) {
		PageQueryResult r = new PageQueryResult();
		p.setRecordCount(countAll(entity));
		r.setPager(p);
		r.setResult(page(createCriteria(entity), p));
		return r;
	}

	//TODO 加入常见方法过滤，例如创建时间、修改时间、名称等

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}

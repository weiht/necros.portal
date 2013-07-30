/**
 * 
 */
package org.necros.portal.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class GeneralDao {
	private SessionFactoryHelper sessionFactoryHelper;
	private IdGenerator idGenerator;
	
	@SuppressWarnings("rawtypes")
	public Map get(String entity, Serializable id) {
		return (Map) sessionFactoryHelper.getSession().get(entity, id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map create(String entity, Map obj) {
		obj.put(GeneralProperties.PROP_ID, idGenerator.generate());
		sessionFactoryHelper.getSession().save(entity, obj);
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
		sessionFactoryHelper.getSession().save(orig);
		return orig;
	}
	
	@SuppressWarnings("rawtypes")
	public Map remove(String entity, Serializable id) {
		Map orig = get(entity, id);
		if (orig != null) {
			sessionFactoryHelper.getSession().delete(entity, orig);
		}
		return orig;
	}
	
	@SuppressWarnings("rawtypes")
	public List all(String entity) {
		return sessionFactoryHelper.createCriteria(entity).list();
	}
	
	public int countAll(String entity) {
		return sessionFactoryHelper.count(sessionFactoryHelper.createCriteria(entity));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PageQueryResult pageAll(String entity, Pager p) {
		PageQueryResult r = new PageQueryResult();
		p.setRecordCount(countAll(entity));
		r.setPager(p);
		r.setResult(sessionFactoryHelper.page(sessionFactoryHelper.createCriteria(entity), p));
		return r;
	}

	//TODO 加入常见方法过滤，例如创建时间、修改时间、名称等

	protected IdGenerator getIdGenerator() {
		return idGenerator;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	protected SessionFactoryHelper getSessionFactoryHelper() {
		return sessionFactoryHelper;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

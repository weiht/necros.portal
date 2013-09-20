/**
 * 
 */
package org.necros.portal.authz.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.authz.Role;
import org.necros.portal.authz.RoleCrudService;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.Person;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 * 
 */
public class RoleCrudServiceH4 implements RoleCrudService {
	private static final Class<?> clazz = Role.class;

	private SessionFactoryHelper sessionFactoryHelper;
	private BasicObjectService basicObjectService;
	private IdGenerator idGenerator;

	public Role get(String id) {
		return (Role) sessionFactoryHelper.getSession().get(clazz, id);
	}

	public Role create(Role r, Person editor) {
		String id = (String) idGenerator.generate();
		basicObjectService.touch(id, Role.ENTITY_NAME, editor == null ? null
				: editor.getId(), editor == null ? null : editor.getInfo()
				.getName());
		r.setId(id);
		sessionFactoryHelper.getSession().save(r);
		return r;
	}

	public Role update(Role r, Person editor) {
		String id = r.getId();
		Role orig = get(id);
		basicObjectService.touch(id, Role.ENTITY_NAME, editor == null ? null
				: editor.getId(), editor == null ? null : editor.getInfo()
				.getName());
		orig.setName(r.getName());
		orig.setDisplayName(r.getDisplayName());
		sessionFactoryHelper.getSession().update(orig);
		return orig;
	}

	public Role remove(String id, Person editor) {
		Role orig = get(id);
		if (orig != null) {
			basicObjectService.touch(id, Role.ENTITY_NAME,
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(orig);
		}
		return orig;
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.getSession().createCriteria(clazz);
	}

	@SuppressWarnings("unchecked")
	public List<Role> all() {
		return createCriteria().list();
	}

	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Role> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return new PageQueryResult<Role>(p, sessionFactoryHelper.page(
				createCriteria(), p));
	}

	private Criteria filterCriteria(Criteria c, String filterText) {
		return c.add(Restrictions.like("name", filterText, MatchMode.ANYWHERE))
				.add(Restrictions.like("displayName", filterText,
						MatchMode.ANYWHERE));
	}

	@SuppressWarnings("unchecked")
	public List<Role> filter(String filterText) {
		return filterCriteria(createCriteria(), filterText).list();
	}

	public int countFiltered(String filterText) {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Role> pageFiltered(Pager p, String filterText) {
		p.setRecordCount(countFiltered(filterText));
		return new PageQueryResult<Role>(p, sessionFactoryHelper.page(
				filterCriteria(createCriteria(), filterText), p));
	}

	public void setSessionFactoryHelper(
			SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setBasicObjectService(BasicObjectService basicObjectService) {
		this.basicObjectService = basicObjectService;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

}

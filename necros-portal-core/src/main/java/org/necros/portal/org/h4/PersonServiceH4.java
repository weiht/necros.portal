/**
 * 
 */
package org.necros.portal.org.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.PasswordEncoder;
import org.necros.portal.org.PasswordGenerator;
import org.necros.portal.org.Person;
import org.necros.portal.org.PersonService;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class PersonServiceH4 implements PersonService {
	protected static final Class<?> clazz = Person.class;
	
	protected SessionFactoryHelper sessionFactoryHelper;
	protected BasicObjectService basicObjectService;
	protected IdGenerator idGenerator;
	protected PasswordEncoder passwordEncoder;
	protected PasswordGenerator passwordGenerator;

	protected void doUpdate(Person p) {
		sessionFactoryHelper.getSession().update(p);
	}
	
	protected Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(clazz);
	}
	
	protected Criteria filterCriteria(Criteria c, String filterText) {
		return c.add(Restrictions.or(
				Restrictions.like("info.name", filterText, MatchMode.ANYWHERE),
				Restrictions.like("info.altName", filterText, MatchMode.ANYWHERE),
				Restrictions.like("loginName", filterText, MatchMode.ANYWHERE)
				));
	}

	public Person get(String id) {
		return (Person) sessionFactoryHelper.getSession().get(clazz, id);
	}

	public Person create(Person p, Person editor) {
		String id = (String) idGenerator.generate();
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		p.setId(id);
		p.setLoginPassword(passwordEncoder.encode(p.getLoginPassword(), p.getLoginName(), p));
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	public Person update(Person p, Person editor) {
		String id = p.getId();
		Person orig = get(id);
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		orig.setInfo(p.getInfo());
		orig.setContact(p.getContact());
		orig.setDisplayOrder(p.getDisplayOrder());
		doUpdate(orig);
		return orig;
	}

	public Person remove(String id, Person editor) {
		Person p = get(id);
		if (p != null) {
			basicObjectService.touch(id, clazz.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(p);
		}
		return p;
	}

	public void changePassword(String id, String oldPwd, String newPwd) {
		Person p = get(id);
		String op = passwordEncoder.encode(oldPwd, p.getLoginName(), p);
		if (op.equals(p.getLoginPassword())) {
			p.setLoginPassword(passwordEncoder.encode(newPwd, p.getLoginName(), p));
		}
		doUpdate(p);
	}

	public String resetPassword(String id) {
		Person p = get(id);
		String newPwd = passwordGenerator.generate();
		p.setLoginPassword(passwordEncoder.encode(newPwd, p.getLoginName(), p));
		doUpdate(p);
		return newPwd;
	}

	@SuppressWarnings("unchecked")
	public List<Person> all() {
		return createCriteria().list();
	}

	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return new PageQueryResult<Person>(p,
				sessionFactoryHelper.page(createCriteria(), p));
	}

	@SuppressWarnings("unchecked")
	public List<Person> filter(String filterText) {
		return filterCriteria(createCriteria(), filterText)
				.list();
	}

	public int countFiltered(String filterText) {
		return sessionFactoryHelper.count(filterCriteria(createCriteria(), filterText));
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageFiltered(Pager p, String filterText) {
		p.setRecordCount(countFiltered(filterText));
		return new PageQueryResult<Person>(p, filterCriteria(createCriteria(), filterText).list());
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setBasicObjectService(BasicObjectService basicObjectService) {
		this.basicObjectService = basicObjectService;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
		this.passwordGenerator = passwordGenerator;
	}
}

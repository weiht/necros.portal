/**
 * 
 */
package org.necros.portal.org.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.HibernateException;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.Organization;
import org.necros.portal.org.OrganizationService;
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
	protected OrganizationService organizationService;

	protected void doUpdate(Person p) {
		sessionFactoryHelper.getSession().update(p);
	}
	
	protected Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(clazz)
				.addOrder(Order.asc("displayOrder"));
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
		Person orig = findOriginalPerson(id);
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
		Person p = findOriginalPerson(id);
		if (p != null) {
			basicObjectService.touch(id, clazz.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(p);
		}
		return p;
	}

	public void changePassword(String id, String oldPwd, String newPwd, Person editor) {
		Person p = findOriginalPerson(id);
		String op = passwordEncoder.encode(oldPwd, p.getLoginName(), p);
		if (op.equals(p.getLoginPassword())) {
			p.setLoginPassword(passwordEncoder.encode(newPwd, p.getLoginName(), p));
			basicObjectService.touch(id, clazz.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			doUpdate(p);
		} else {
			throw new HibernateException("Old password does not match.");
		}
	}

	public String resetPassword(String id, Person editor) {
		Person p = findOriginalPerson(id);
		String newPwd = passwordGenerator.generate();
		p.setLoginPassword(passwordEncoder.encode(newPwd, p.getLoginName(), p));
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
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

	public Person updateMembership(Person p, String orgId, Person editor) {
		Person orig = findOriginalPerson(p.getId());
		if (orgId == null) {
			p.setOrgId(null);
			p.setOrgPath(null);
		} else {
			Organization org = findOrg(orgId);
			p.setOrgId(org.getId());
			p.setOrgPath(org.getPath());
		}
		
		basicObjectService.touch(orig.getId(), clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		doUpdate(orig);
		return orig;
	}

	protected Person findOriginalPerson(String id) {
		Person orig = get(id);
		if (orig == null) {
			throw new HibernateException("No such person.");
		}
		return orig;
	}

	protected Organization findOrg(String orgId) {
		Organization org = organizationService.get(orgId);
		if (org == null) {
			throw new HibernateException("No such organization.");
		}
		return org;
	}

	private Criteria orgDirectMemberCriteria(String orgId) {
		return createCriteria().add(Restrictions.eq("orgId", orgId));
	}

	@SuppressWarnings("unchecked")
	public List<Person> orgAllDirectMembers(String orgId) {
		return orgDirectMemberCriteria(orgId)
				.list();
	}

	public int countOrgAllDirectMembers(String orgId) {
		return sessionFactoryHelper.count(orgDirectMemberCriteria(orgId));
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageOrgAllDirectMembers(Pager p, String orgId) {
		p.setRecordCount(countOrgAllDirectMembers(orgId));
		return new PageQueryResult<Person>(p,
				sessionFactoryHelper.page(orgDirectMemberCriteria(orgId), p));
	}

	private Criteria orgDescendMemberCriteria(Organization org) {
		return createCriteria().add(Restrictions.like("orgPath", org.getPath() + Organization.SPLITTER, MatchMode.END));
	}

	@SuppressWarnings("unchecked")
	public List<Person> orgAllDescendMembers(String orgId) {
		Organization o = findOrg(orgId);
		return orgDescendMemberCriteria(o)
				.list();
	}

	public int countOrgAllDescendMembers(String orgId) {
		Organization o = findOrg(orgId);
		return doCountOrgAllDecendMembers(o);
	}

	private int doCountOrgAllDecendMembers(Organization o) {
		return sessionFactoryHelper.count(orgDescendMemberCriteria(o));
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageOrgAllDescendMembers(Pager p,
			String orgId) {
		Organization o = findOrg(orgId);
		p.setRecordCount(doCountOrgAllDecendMembers(o));
		return new PageQueryResult<Person>(p,
				sessionFactoryHelper.page(orgDescendMemberCriteria(o), p));
	}

	@SuppressWarnings("unchecked")
	public List<Person> orgFilteredDirectMembers(String orgId, String filter) {
		return filterCriteria(orgDirectMemberCriteria(orgId), filter)
				.list();
	}

	public int countOrgFilteredDirectMembers(String orgId, String filter) {
		return sessionFactoryHelper.count(filterCriteria(orgDirectMemberCriteria(orgId), filter));
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageOrgFilteredDirectMembers(Pager p,
			String orgId, String filter) {
		p.setRecordCount(countOrgFilteredDirectMembers(orgId, filter));
		return new PageQueryResult<Person>(p,
				sessionFactoryHelper.page(filterCriteria(orgDirectMemberCriteria(orgId), filter), p));
	}

	@SuppressWarnings("unchecked")
	public List<Person> orgFilteredDescendMembers(String orgId, String filter) {
		Organization o = findOrg(orgId);
		return filterCriteria(orgDescendMemberCriteria(o), filter)
				.list();
	}

	public int countOrgFilteredDescendMembers(String orgId, String filter) {
		Organization o = findOrg(orgId);
		return doCountOrgFilteredDecendMembers(o, filter);
	}

	private int doCountOrgFilteredDecendMembers(Organization o, String filter) {
		return sessionFactoryHelper.count(filterCriteria(orgDescendMemberCriteria(o), filter));
	}

	@SuppressWarnings("unchecked")
	public PageQueryResult<Person> pageOrgFilteredDescendMembers(Pager p,
			String orgId, String filter) {
		Organization o = findOrg(orgId);
		p.setRecordCount(doCountOrgFilteredDecendMembers(o, filter));
		return new PageQueryResult<Person>(p,
				sessionFactoryHelper.page(filterCriteria(orgDescendMemberCriteria(o), filter), p));
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

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}

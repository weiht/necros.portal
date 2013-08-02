/**
 * 
 */
package org.necros.portal.org.h4;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.Organization;
import org.necros.portal.org.OrganizationService;
import org.necros.portal.org.Person;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class OrganizationServiceH4 implements OrganizationService {
	private static final String HQL_QUERY_PATH = "from Organization o"
			+ " where ? like o.path + '%'";
	
	private SessionFactoryHelper sessionFactoryHelper;
	private BasicObjectService basicObjectService;
	private IdGenerator idGenerator;
	
	protected Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(Organization.class);
	}

	public Organization get(String id) {
		return (Organization) sessionFactoryHelper.getSession().get(Organization.class, id);
	}

	public Organization find(String path) {
		return (Organization) createCriteria()
				.add(Restrictions.eq("path", path))
				.uniqueResult();
	}

	public Organization create(Organization org, Person editor) {
		String id = (String) idGenerator.generate();
		basicObjectService.touch(id, Organization.class.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		org.setId(id);
		String pid = org.getParentId();
		Organization porg = get(pid);
		if (porg == null) {
			org.setPath(Organization.SPLITTER + org.getName());
			org.setLevel(1);
		} else {
			org.setPath(porg.getPath() + Organization.SPLITTER + org.getName());
			org.setLevel(porg.getLevel() + 1);
		}
		sessionFactoryHelper.getSession().save(org);
		return org;
	}

	public Organization update(Organization org, Person editor) {
		String id = org.getId();
		Organization orig = get(id);
		basicObjectService.touch(id, Organization.class.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		orig.setName(org.getName());
		orig.setAltName(org.getAltName());
		orig.setContact(org.getContact());
		orig.setDescription(org.getDescription());
		orig.setDisplayOrder(org.getDisplayOrder());
		sessionFactoryHelper.getSession().update(orig);
		return org;
	}

	public Organization remove(String id, Person editor) {
		Organization org = get(id);
		if (org != null) {
			basicObjectService.touch(id, Organization.class.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(org);
		}
		return org;
	}

	@SuppressWarnings("unchecked")
	public List<Organization> root() {
		return createCriteria().add(Restrictions.isNull("parentId"))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Organization> children(String id) {
		return createCriteria().add(Restrictions.eq("parentId", id))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Organization> path(String id) {
		Organization org = get(id);
		if (org == null) {
			return new ArrayList<Organization>();
		} else {
			return sessionFactoryHelper.getSession()
					.createQuery(HQL_QUERY_PATH)
					.setString(0, id)
					.list();
		}
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
}

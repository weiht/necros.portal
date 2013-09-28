/**
 * 
 */
package org.necros.portal.authz.h4;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.criterion.Restrictions;
import org.necros.portal.authz.Role;
import org.necros.portal.authz.RoleMember;
import org.necros.portal.authz.RoleMemberService;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.Person;
import org.necros.portal.util.SessionFactoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weiht
 *
 */
public class RoleMemberServiceH4 implements RoleMemberService {
	private static final Logger logger = LoggerFactory.getLogger(RoleMemberServiceH4.class);

	private static final String HQL_QUERY_PERSON_IDS =
			"select m.personId from RoleMember m where m.roleId = ?";
	private static final String HQL_QUERY_ROLE_IDS =
			"select m.roleId from RoleMember m where m.personId = ?";

	private static final Class<?> clazz = RoleMember.class;

	private SessionFactoryHelper sessionFactoryHelper;
	private BasicObjectService basicObjectService;
	private IdGenerator idGenerator;

	@SuppressWarnings("unchecked")
	public List<Person> allMembers(String roleId) {
		List<String> rids = sessionFactoryHelper.getSession()
				.createQuery(HQL_QUERY_PERSON_IDS)
				.setString(0, roleId)
				.list();
		if (rids == null | rids.isEmpty()) {
			return new ArrayList<Person>();
		}
		return sessionFactoryHelper.createCriteria(Person.class)
				.add(Restrictions.in("id", rids))
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<Role> allRoles(String personId) {
		List<String> rids = sessionFactoryHelper.getSession()
				.createQuery(HQL_QUERY_ROLE_IDS)
				.setString(0, personId)
				.list();
		return sessionFactoryHelper.createCriteria(Role.class)
				.add(Restrictions.in("id", rids))
				.list();
	}

	public RoleMember addMember(String roleId, String personId, Person editor) {
		RoleMember m = getMembership(roleId, personId);
		if (m == null) {
			logger.debug("No such membership. Creating...");
			String id = (String) idGenerator.generate();
			basicObjectService.touch(id, RoleMember.ENTITY_NAME, editor == null ? null
					: editor.getId(), editor == null ? null : editor.getInfo()
					.getName());
			m = new RoleMember();
			m.setId(id);
			m.setRoleId(roleId);
			m.setPersonId(personId);
			sessionFactoryHelper.getSession().save(m);
			logger.debug("Membership created.");
		}
		return m;
	}

	public void removeMember(String roleId, String personId, Person editor) {
		remove(roleId, personId, editor);
	}

	public RoleMember remove(String roleId, String personId, Person editor) {
		RoleMember m = getMembership(roleId, personId);
		if (m != null) {
			basicObjectService.touch(m.getId(), RoleMember.ENTITY_NAME, editor == null ? null
					: editor.getId(), editor == null ? null : editor.getInfo()
					.getName());
			sessionFactoryHelper.getSession().delete(m);
		}
		return m;
	}

	public RoleMember getMembership(String roleId, String personId) {
		RoleMember m = (RoleMember) sessionFactoryHelper.getSession().createCriteria(clazz)
			.add(Restrictions.eq("roleId", roleId))
			.add(Restrictions.eq("personId", personId))
			.uniqueResult();
		logger.trace("Membership found for role {} and person {}: {}", roleId, personId, m);
		return m;
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

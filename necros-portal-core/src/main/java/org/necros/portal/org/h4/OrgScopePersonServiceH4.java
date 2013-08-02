/**
 * 
 */
package org.necros.portal.org.h4;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.portal.org.Organization;
import org.necros.portal.org.OrganizationService;
import org.necros.portal.org.Person;
import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class OrgScopePersonServiceH4 extends PersonServiceH4 {
	private static final String MSG_PERSON_NOT_IN_SCOPE = "Person not in the specified organization.";
	private String rootOrgId;
	private String rootOrgPath;
	private OrganizationService organizationService;
	
	private synchronized String ensureRootOrg() {
		if (rootOrgPath != null) return rootOrgPath;
		if (StringUtils.hasText(rootOrgId)) {
			Organization rootOrg = organizationService.get(rootOrgId);
			if (rootOrg == null) {
				rootOrgPath = Organization.SPLITTER + Organization.SPLITTER;
			} else {
				rootOrgPath = rootOrg.getPath();
			}
		}
		return rootOrgPath;
	}
	
	private boolean isInScope(Person p) {
		if (!StringUtils.hasText(rootOrgId)) {
			if (!StringUtils.hasText(p.getOrgId())) {
				p.setOrgPath(Organization.SPLITTER);
				return true;
			} else {
				return false;
			}
		} else {
			Organization org = organizationService.get(p.getOrgId());
			if (org == null) return false;
			ensureRootOrg();
			if (org.getPath().indexOf(rootOrgPath) < 0) return false;
			return true;
		}
	}
	
	@Override
	public Person get(String id) {
		Person p = super.get(id);
		if (p == null) return null;
		if (isInScope(p)) return p;
		return null;
	}
	
	@Override
	public Person create(Person p, Person editor) {
		if (!isInScope(p)) {
			p.setOrgId(rootOrgId);
			p.setOrgPath(rootOrgPath);
		}
		return super.create(p, editor);
	}
	
	@Override
	public Person update(Person p, Person editor) {
		if (!isInScope(p)) {
			throw new HibernateException(MSG_PERSON_NOT_IN_SCOPE);
		}
		return super.update(p, editor);
	}
	
	@Override
	public Person remove(String id, Person editor) {
		Person p = get(id);
		if (p == null) throw new HibernateException(MSG_PERSON_NOT_IN_SCOPE);
		return super.remove(id, editor);
	}
	
	@Override
	protected Criteria createCriteria() {
		if (StringUtils.hasText(rootOrgId)) {
			return super.createCriteria()
					.add(Restrictions.like("orgPath", rootOrgPath + Organization.SPLITTER, MatchMode.START));
		} else {
			return super.createCriteria().add(Restrictions.isNull("orgId"));
		}
	}

	public String getRootOrgId() {
		return rootOrgId;
	}

	public void setRootOrgId(String rootOrgId) {
		this.rootOrgId = rootOrgId;
	}

	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}

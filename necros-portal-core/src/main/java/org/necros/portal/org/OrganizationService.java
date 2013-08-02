/**
 * 
 */
package org.necros.portal.org;

import java.util.List;

/**
 * @author weiht
 *
 */
public interface OrganizationService {
	public abstract Organization get(String id);
	public abstract Organization find(String path);
	public abstract Organization create(Organization org, Person editor);
	public abstract Organization update(Organization org, Person editor);
	public abstract Organization remove(String id, Person editor);
	
	public abstract List<Organization> root();
	public abstract List<Organization> children(String id);
	public abstract List<Organization> path(String id);
	public abstract List<Organization> decendents(String rootId);
}

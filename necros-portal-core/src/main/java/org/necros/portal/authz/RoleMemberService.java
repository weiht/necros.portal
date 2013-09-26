/**
 * 
 */
package org.necros.portal.authz;

import java.util.List;

import org.necros.portal.org.Person;

/**
 * @author weiht
 *
 */
public interface RoleMemberService {
	public abstract List<Person> allMembers(String roleId);
	public abstract RoleMember addMember(String roleId, String personId, Person editor);
	public abstract void removeMember(String roleId, String personId, Person editor);
	public abstract RoleMember remove(String roleId, String personId, Person editor);
	public abstract RoleMember getMembership(String roleId, String personId);
	public abstract List<Role> allRoles(String personId);
}

/**
 * 
 */
package org.necros.portal.authz;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@Entity(name=RoleMember.ENTITY_NAME) @Table(name="portal_authz_role_members")
public class RoleMember {
	public static final String ENTITY_NAME = "RoleMember";
	
	@Id @Column(name="role_member_id")
	private String id;
	@Column(name="role_id", length=200, nullable=false)
	private String roleId;
	@Column(name="person_id", length=200, nullable=false)
	private String personId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}

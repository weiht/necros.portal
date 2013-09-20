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
@Entity(name=Role.ENTITY_NAME) @Table(name="portal_authz_roles")
public class Role {
	public static final String ENTITY_NAME = "Role";
	
	@Id @Column(name="role_id")
	private String id;
	@Column(name="role_name", length=200, nullable=false)
	private String name;
	@Column(name="display_name", length=200)
	private String displayName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

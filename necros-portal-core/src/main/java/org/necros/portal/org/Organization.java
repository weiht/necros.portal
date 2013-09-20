/**
 * 
 */
package org.necros.portal.org;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity(name=Organization.ENTITY_NAME) @Table(name="portal_organizations")
public class Organization implements Serializable {
	public static final String ENTITY_NAME = "Organization";
	public static final String SPLITTER = "/";
	
	@Id @Column(name="org_id", length=64)
	private String id;
	@Column(name="parent_id", length=64)
	private String parentId;
	@Column(name="org_path", length=2000, nullable=false)
	private String path;
	@Column(name="org_level", nullable=false)
	private Integer level;
	@Column(name="org_name", length=200, nullable=false)
	private String name;
	@Column(name="alt_name", length=200)
	private String altName;
	@Column(name="org_type", length=200, nullable=false)
	private String type;
	@Column(name="org_description", length=200)
	private String description;
	@Column(name="display_order", nullable=false)
	private Integer displayOrder;
	@Embedded
	private ComplexContact contact = new ComplexContact();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAltName() {
		return altName;
	}

	public void setAltName(String altName) {
		this.altName = altName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComplexContact getContact() {
		return contact;
	}

	public void setContact(ComplexContact contact) {
		this.contact = contact;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}

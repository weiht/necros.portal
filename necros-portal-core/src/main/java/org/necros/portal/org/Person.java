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
@Entity(name=Person.ENTITY_NAME) @Table(name="portal_persons")
public class Person implements Serializable {
	public static final String ENTITY_NAME = "Person";
	
	@Id @Column(name="person_id", length=200)
	private String id;
	@Column(name="login_name", length=200, nullable=false)
	private String loginName;
	@Column(name="login_password", length=200)
	private String loginPassword;
	@Column(name="org_id", length=200)
	private String orgId;
	@Column(name="org_path", length=2000)
	private String orgPath;
	@Embedded
	private PersonInfo info = new PersonInfo();
	@Column(name="display_order", nullable=false)
	private Integer displayOrder;
	@Embedded
	private ComplexContact contact = new ComplexContact();

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public PersonInfo getInfo() {
		return info;
	}

	public void setInfo(PersonInfo info) {
		this.info = info;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public ComplexContact getContact() {
		return contact;
	}

	public void setContact(ComplexContact contact) {
		this.contact = contact;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgPath() {
		return orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
}

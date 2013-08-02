/**
 * 
 */
package org.necros.portal.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity @Table(name="portal_basic_object")
public class BasicObject implements Serializable {
	@Id @Column(name="obj_id", length=200)
	private String id;
	@Column(name="obj_entity", length=200, nullable=false)
	private String entity;
	@Column(name="display_name", length=200)
	private String displayName;
	@Column(name="owner_id", length=200)
	private String ownerId;
	@Column(name="owner_name", length=200)
	private String ownerName;
	@Column(name="create_person_id", length=200)
	private String createPersonId;
	@Column(name="create_person_name", length=200)
	private String createPersonName;
	@Column(name="create_time")
	private Timestamp createTime;
	@Column(name="update_person_id", length=200)
	private String updatePersonId;
	@Column(name="update_person_name", length=200)
	private String updatePersonName;
	@Column(name="update_time")
	private Timestamp updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreatePersonId() {
		return createPersonId;
	}

	public void setCreatePersonId(String createPersonId) {
		this.createPersonId = createPersonId;
	}

	public String getCreatePersonName() {
		return createPersonName;
	}

	public void setCreatePersonName(String createPersonName) {
		this.createPersonName = createPersonName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getUpdatePersonId() {
		return updatePersonId;
	}

	public void setUpdatePersonId(String updatePersonId) {
		this.updatePersonId = updatePersonId;
	}

	public String getUpdatePersonName() {
		return updatePersonName;
	}

	public void setUpdatePersonName(String updatePersonName) {
		this.updatePersonName = updatePersonName;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}

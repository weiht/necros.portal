/**
 * 
 */
package org.necros.portal.data;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity @Table(name="portal_object_touch_log")
public class ObjectTouchLog implements Serializable {
	@Id @GeneratedValue
	private Long id;
	@Column(name="obj_id", length=200, nullable=false)
	private String objectId;
	@Column(name="obj_entity", length=200)
	private String entity;
	@Column(name="update_person_id", length=200)
	private String updatePersonId;
	@Column(name="update_person_name", length=200)
	private String updatePersonName;
	@Column(name="update_time")
	private Timestamp updateTime;
	@Column(name="log_content") @Lob
	private String log;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
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

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
}

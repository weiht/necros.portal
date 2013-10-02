/**
 * 
 */
package org.necros.portal.ajax;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@Entity(name="AjaxCall") @Table(name="portal_ajax_calls")
public class AjaxCall implements Serializable {
	private static final long serialVersionUID = -3088436302362776417L;

	@Id @Column(name="ajax_call_id", length=200)
	private String id;
	@Column(name="owner_id", length=200)
	private String ownerId;
	@Column(name="display_name", length=200)
	private String displayName;
	@Column(name="result_type", length=20)
	private String resultType;
	@Column(name="ajax_template") @Lob
	private String template;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Override
	public String toString() {
		return AjaxCall.class.getName() + " - " + id;
	}
}

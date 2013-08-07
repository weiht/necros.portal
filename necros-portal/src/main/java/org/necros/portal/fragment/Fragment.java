/**
 * 
 */
package org.necros.portal.fragment;

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
@Entity(name="Fragment") @Table(name="portal_fragments")
public class Fragment implements Serializable {
	private static final long serialVersionUID = 6177984132876654977L;

	@Id @Column(name="fragment_id", length=2000)
	private String id;
	@Column(name="owner_id", length=200)
	private String ownerId;
	@Column(name="display_name", length=200)
	private String displayName;
	@Column(name="result_type", length=20)
	private String resultType;
	@Column(name="fragment_template") @Lob
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
}

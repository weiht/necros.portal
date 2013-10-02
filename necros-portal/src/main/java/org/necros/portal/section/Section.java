/**
 * 
 */
package org.necros.portal.section;

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
@Entity(name="Section") @Table(name="portal_sections")
public class Section implements Serializable {
	private static final long serialVersionUID = -7568751035115556298L;

	@Id @Column(name="section_id", length=200)
	private String id;
	@Column(name="owner_id", length=200)
	private String ownerId;
	@Column(name="display_name", length=200)
	private String displayName;
	@Column(name="html_template") @Lob
	private String template;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	@Override
	public String toString() {
		return Section.class.getName() + " - " + id;
	}
}

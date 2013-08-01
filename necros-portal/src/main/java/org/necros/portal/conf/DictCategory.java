/**
 * 
 */
package org.necros.portal.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity @Table(name="portal_dict_categories")
public class DictCategory implements Serializable {
	@Id @Column(name="cat_name", length=200)
	private String name;
	@Column(name="display_name", length=200, nullable=false)
	private String displayName;
	@Column(name="cat_description", length=200)
	private String description;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

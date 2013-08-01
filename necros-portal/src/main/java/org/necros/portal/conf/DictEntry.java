/**
 * 
 */
package org.necros.portal.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity @Table(name="portal_dict_entries")
public class DictEntry implements Serializable {
	@Id @GeneratedValue @Column(name="entry_id")
	private Integer id;
	@Column(name="cat_id", nullable=false, length=200)
	private String categoryId;
	@Column(name="entry_key", nullable=false, length=200)
	private String key;
	@Column(name="display_text", nullable=false, length=200)
	private String displayText;
	@Column(name="entry_description", length=200)
	private String description;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}

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
	@Column(name="display_order", nullable=false)
	private Integer displayOrder = 0;

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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer order) {
		this.displayOrder = order;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String text) {
		this.displayText = text;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("Class=").append(DictEntry.class).append('\n');
		buff.append("id=").append(id).append('\n');
		buff.append("categoryId=").append(categoryId).append('\n');
		buff.append("displayOrder=").append(displayOrder).append('\n');
		buff.append("key=").append(key).append('\n');
		buff.append("displayText=").append(displayText).append('\n');
		buff.append("description=").append(description).append('\n');
		return buff.toString();
	}
}

/**
 * 
 */
package org.necros.portal.conf;

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
@SuppressWarnings("serial")
@Entity @Table(name="portal_params")
public class SysParam implements Serializable {
	public static final String splitter = "/";
	@Id @Column(name="param_key", length=255)
	private String key;
	@Column(name="param_value") @Lob
	private String value;
	@Column(name="param_description", length=200)
	private String description;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

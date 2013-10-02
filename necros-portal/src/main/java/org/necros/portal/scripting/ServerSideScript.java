/**
 * 
 */
package org.necros.portal.scripting;

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
@Entity @Table(name="portal_server_side_scripts")
public class ServerSideScript implements Serializable {
	@Id @Column(name="script_name", length=200)
	private String name;
	@Column(name="script_engine", length=50, nullable=false)
	private String engine;
	@Column(name="display_name", length=200, nullable=false)
	private String displayName;
	@Column(name="script_description", length=200)
	private String description;
	@Column(name="source_code") @Lob
	private String sourceCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
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

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
}

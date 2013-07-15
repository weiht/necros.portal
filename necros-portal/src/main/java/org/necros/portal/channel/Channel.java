package org.necros.portal.channel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name="Channel") @Table(name="portal_channels")
public class Channel implements Serializable {
	private static final long serialVersionUID = -3581831870658374484L;

	@Id @Column(name="channel_id", length=200)
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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}
	
	@Override
	public String toString() {
		return Channel.class.getName() + " - " + id;
	}
}

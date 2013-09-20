/**
 * 
 */
package org.necros.portal.menu;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
@Entity(name=MenuItem.ENTITY_NAME) @Table(name="portal_menu_items")
public class MenuItem implements Serializable {
	public static final String ENTITY_NAME = "MenuItem";
	public static final String SPLITTER = "/";
	public static final Integer USABLE = 0;
	
	@Id @Column(name="item_id", length=64)
	private String id;
	@Column(name="parent_id", length=64)
	private String parentId;
	@Column(name="item_path", length=2000, nullable=false)
	private String path;
	@Column(name="item_name", length=200, nullable=false)
	private String name;
	@Column(name="display_name", length=200, nullable=false)
	private String displayName;
	@Column(name="display_order", nullable=false)
	private Integer displayOrder;
	@Column(name="usable_stauts", nullable=false)
	private Integer usableStatus;
	@Column(name="channel_id", length=2000)
	private String channelId;
	@Column(name="destination_url", length=2000)
	private String url;
	@Transient
	private List<MenuItem> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getUsableStatus() {
		return usableStatus;
	}

	public void setUsableStatus(Integer usableStatus) {
		this.usableStatus = usableStatus;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}
}

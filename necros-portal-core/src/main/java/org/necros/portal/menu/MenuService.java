/**
 * 
 */
package org.necros.portal.menu;

import java.util.List;

import org.necros.portal.org.Person;

/**
 * @author weiht
 *
 */
public interface MenuService {
	public abstract MenuItem get(String id);
	public abstract MenuItem create(MenuItem itm, Person editor);
	public abstract MenuItem update(MenuItem itm, Person editor);
	public abstract MenuItem remove(String id, Person editor);
	
	public abstract List<MenuItem> root();
	public abstract List<MenuItem> children(String parentId);
	
	public abstract List<MenuItem> allRoot();
	public abstract List<MenuItem> allChildren(String parentId);
}

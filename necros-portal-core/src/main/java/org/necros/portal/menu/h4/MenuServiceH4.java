/**
 * 
 */
package org.necros.portal.menu.h4;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.menu.MenuItem;
import org.necros.portal.menu.MenuService;
import org.necros.portal.org.Person;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class MenuServiceH4 implements MenuService {
	public static final Class<?> clazz = MenuItem.class;
	public static final String HQL_PATH_TO_ROOT =
		"from MenuItem i where ? like concat(i.path, '"
		+ MenuItem.SPLITTER + "%')";

	private BasicObjectService basicObjectService;
	private IdGenerator idGenerator;
	
	private SessionFactoryHelper sessionFactoryHelper;

	public MenuItem get(String id) {
		return (MenuItem) sessionFactoryHelper.getSession().get(clazz, id);
	}

	public MenuItem create(MenuItem itm, Person editor) {
		String id = (String) idGenerator.generate();
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		itm.setId(id);
		generatePath(itm);
		sessionFactoryHelper.getSession().save(itm);
		return itm;
	}

	private void generatePath(MenuItem itm) {
		String pid = itm.getParentId();
		MenuItem pitm = get(pid);
		if (pitm == null) {
			itm.setPath(MenuItem.SPLITTER + itm.getName());
		} else {
			itm.setPath(pitm.getPath() + MenuItem.SPLITTER + itm.getName());
		}
	}

	public MenuItem update(MenuItem itm, Person editor) {
		String id = itm.getId();
		MenuItem orig = get(id);
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		orig.setName(itm.getName());
		generatePath(orig);
		orig.setDisplayName(itm.getDisplayName());
		orig.setChannelId(itm.getChannelId());
		orig.setDisplayOrder(itm.getDisplayOrder());
		orig.setUrl(itm.getUrl());
		orig.setUsableStatus(itm.getUsableStatus());
		sessionFactoryHelper.getSession().update(orig);
		return orig;
	}

	public MenuItem remove(String id, Person editor) {
		MenuItem orig = get(id);
		if (orig != null) {
			basicObjectService.touch(id, clazz.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(orig);
		}
		return orig;
	}
	
	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(clazz);
	}
	
	private Criteria rootCriteria() {
		return createCriteria().add(Restrictions.eqOrIsNull("parentId", ""));
	}
	
	private Criteria childrenCriteria(String id) {
		return createCriteria().add(Restrictions.eq("parentId", id));
	}

	private Criteria usable(Criteria c) {
		return c.add(Restrictions.eq("usableStatus", MenuItem.USABLE));
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> root() {
		return usable(rootCriteria())
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> children(String parentId) {
		return usable(childrenCriteria(parentId))
				.list();
	}

	private void fetchChildren(List<MenuItem> items) {
		for (MenuItem itm: items) {
			List<MenuItem> children = children(itm.getId());
			if (children != null && !children.isEmpty()) {
				itm.setChildren(children);
				fetchChildren(children);
			}
		}
	}
	
	public List<MenuItem> display(String id) {
		List<MenuItem> root = children(id);
		fetchChildren(root);
		return root;
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> allRoot() {
		return rootCriteria()
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> allChildren(String parentId) {
		return childrenCriteria(parentId)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<MenuItem> pathToRoot(MenuItem leaf) {
		List<MenuItem> itm = sessionFactoryHelper.getSession().createQuery(HQL_PATH_TO_ROOT)
			.setString(0, leaf.getPath())
			.list();
		itm.add(leaf);
		return itm;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setBasicObjectService(BasicObjectService basicObjectService) {
		this.basicObjectService = basicObjectService;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}
}

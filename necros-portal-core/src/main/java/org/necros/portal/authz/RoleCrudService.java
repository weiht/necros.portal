package org.necros.portal.authz;

import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.org.Person;

public interface RoleCrudService {
	public abstract Role get(String id);
	public abstract Role create(Role r, Person editor);
	public abstract Role update(Role r, Person editor);
	public abstract Role remove(String id, Person editor);
	
	public abstract List<Role> all();
	public abstract int countAll();
	public abstract PageQueryResult<Role> pageAll(Pager p);
	
	public abstract List<Role> filter(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract PageQueryResult<Role> pageFiltered(Pager p, String filterText);
}

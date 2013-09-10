/**
 * 
 */
package org.necros.portal.org;

import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

/**
 * @author weiht
 *
 */
public interface PersonService {
	public abstract Person get(String id);
	public abstract Person create(Person p, Person editor);
	public abstract Person update(Person p, Person editor);
	public abstract Person remove(String id, Person editor);
	
	public abstract void changePassword(String id, String oldPwd, String newPwd, Person editor);
	public abstract String resetPassword(String id, Person editor);
	
	public abstract List<Person> all();
	public abstract int countAll();
	public abstract PageQueryResult<Person> pageAll(Pager p);
	
	public abstract List<Person> filter(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract PageQueryResult<Person> pageFiltered(Pager p, String filterText);
}

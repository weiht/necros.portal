/**
 * 
 */
package org.necros.portal.conf;

import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

/**
 * @author weiht
 *
 */
public interface EntryService {
	public abstract void setCategoryId(String categoryId);
	public abstract DictEntry get(String key);
	public abstract DictEntry create(DictEntry p);
	public abstract DictEntry update(DictEntry p);
	public abstract DictEntry remove(String key);
	public abstract void removeAll();
	public abstract List<DictEntry> all();
	public abstract int countAll();
	public abstract PageQueryResult<DictEntry> pageAll(Pager p);
	public abstract List<DictEntry> filter(String filter);
	public abstract int countFiltered(String filter);
	public abstract PageQueryResult<DictEntry> pageFiltered(Pager p, String filter);
}

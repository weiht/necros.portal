/**
 * 
 */
package org.necros.portal.conf;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

/**
 * @author weiht
 *
 */
public interface CategoryService {
	public abstract DictCategory get(String key);
	public abstract DictCategory create(DictCategory p);
	public abstract DictCategory update(DictCategory p);
	public abstract DictCategory remove(String key);
	public abstract List<DictCategory> all();
	public abstract int countAll();
	public abstract PageQueryResult<DictCategory> pageAll(Pager p);
	public abstract List<DictCategory> filter(String filter);
	public abstract int countFiltered(String filter);
	public abstract PageQueryResult<DictCategory> pageFiltered(Pager p, String filter);
	public abstract String exportAll() throws IOException;
	public abstract void importAll(String fn) throws IOException;
}

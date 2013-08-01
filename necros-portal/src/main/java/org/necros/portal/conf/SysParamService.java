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
public interface SysParamService {
	public abstract SysParam get(String key);
	public abstract SysParam eval(String key);
	public abstract SysParam create(SysParam p);
	public abstract SysParam update(SysParam p);
	public abstract SysParam remove(String key);
	public abstract List<SysParam> all();
	public abstract int countAll();
	public abstract PageQueryResult<SysParam> pageAll(Pager p);
	public abstract List<SysParam> filter(String filter);
	public abstract int countFiltered(String filter);
	public abstract PageQueryResult<SysParam> pageFiltered(Pager p, String filter);
}

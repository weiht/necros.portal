/**
 * 
 */
package org.necros.portal.scripting;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

/**
 * @author weiht
 *
 */
public interface ScriptService {
	public abstract ServerSideScript get(String name);
	public abstract ServerSideScript create(ServerSideScript s);
	public abstract ServerSideScript update(ServerSideScript s);
	public abstract ServerSideScript remove(String name);
	public abstract List<ServerSideScript> all();
	public abstract int countAll();
	public abstract PageQueryResult<ServerSideScript> pageAll(Pager p);
	public abstract List<ServerSideScript> filter(String filterText);
	public abstract int countFiltered(String filterText);
	public abstract PageQueryResult<ServerSideScript> pageFiltered(Pager p, String filterText);
	public abstract String exportAll() throws IOException;
	public abstract void importAll(String fn) throws IOException;
}

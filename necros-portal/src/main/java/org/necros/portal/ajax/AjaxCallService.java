package org.necros.portal.ajax;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

public interface AjaxCallService {
	List<AjaxCall> callsOwnedBy(String ownerId);
	AjaxCall findById(String id);
	void save(AjaxCall call);
	void remove(String id);
	List<AjaxCall> all();
	int countAll();
	PageQueryResult<AjaxCall> pageAll(Pager pager);
	List<AjaxCall> filter(String filterText);
	int countFiltered(String filterText);
	PageQueryResult<AjaxCall> pageFiltered(Pager pager, String filterText);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

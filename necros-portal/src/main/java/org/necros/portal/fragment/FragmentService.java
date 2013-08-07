package org.necros.portal.fragment;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

public interface FragmentService {
	List<Fragment> fragmentsOwnedBy(String ownerId);
	Fragment findById(String id);
	void save(Fragment frag);
	void remove(String id);
	List<Fragment> all();
	int countAll();
	PageQueryResult<Fragment> pageAll(Pager pager);
	List<Fragment> filter(String filterText);
	int countFiltered(String filterText);
	PageQueryResult<Fragment> pageFiltered(Pager pager, String filterText);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

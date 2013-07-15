package org.necros.portal.fragment;

import java.io.IOException;
import java.util.List;

public interface FragmentService {
	List<Fragment> fragmentsOwnedBy(String ownerId);
	Fragment findById(String id);
	void save(Fragment frag);
	void remove(String id);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

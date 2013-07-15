package org.necros.portal.ajax;

import java.io.IOException;
import java.util.List;

public interface AjaxCallService {
	List<AjaxCall> callsOwnedBy(String ownerId);
	AjaxCall findById(String id);
	void save(AjaxCall call);
	void remove(String id);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

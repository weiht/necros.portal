/**
 * 
 */
package org.necros.portal.conf.h4;

import java.util.Map;

import org.necros.portal.conf.EntryService;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class EntryServiceFactoryH4 {
	private SessionFactoryHelper sessionFactoryHelper;
	private Map<String, EntryService> storage;
	
	public EntryService get(String categoryId) {
		EntryService svc = storage.get(categoryId);
		if (svc == null) {
			svc = constructService(categoryId);
		}
		return svc;
	}

	/**
	 * @param categoryId
	 * @return
	 */
	private EntryService constructService(String categoryId) {
		EntryServiceH4 svc = new EntryServiceH4();
		svc.setCategoryId(categoryId);
		svc.setSessionFactoryHelper(sessionFactoryHelper);
		storage.put(categoryId, svc);
		return svc;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

/**
 * 
 */
package org.necros.portal.conf.h4;

import java.util.HashMap;
import java.util.Map;

import org.necros.portal.conf.EntryService;
import org.necros.portal.conf.EntryServiceFactory;

import org.springframework.context.ApplicationContext;  
import org.springframework.context.ApplicationContextAware;  

/**
 * @author weiht
 *
 */
public class EntryServiceFactoryH4 implements EntryServiceFactory, ApplicationContextAware {
	private Map<String, EntryService> storage = new HashMap<String, EntryService>();
	private ApplicationContext context;
	
	@Override
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
		EntryService svc = (EntryService)context.getBean("p.entryService");
		svc.setCategoryId(categoryId);
		storage.put(categoryId, svc);
		return svc;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}
}

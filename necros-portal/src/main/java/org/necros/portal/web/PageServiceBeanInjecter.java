/**
 * 
 */
package org.necros.portal.web;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author weiht
 *
 */
public class PageServiceBeanInjecter {
	private PageService pageService;
	private Map<String, Object> serviceBeans;
	
	public void doInject() {
		if (!serviceBeans.isEmpty()) {
			for (Entry<String, Object> e: serviceBeans.entrySet()) {
				pageService.registerServiceBean(e.getKey(), e.getValue());
			}
		}
	}

	public PageService getPageService() {
		return pageService;
	}

	@Required
	public void setPageService(PageService service) {
		this.pageService = service;
	}

	public Map<String, Object> getServiceBeans() {
		return serviceBeans;
	}

	@Required
	public void setServiceBeans(Map<String, Object> serviceBeans) {
		this.serviceBeans = serviceBeans;
	}
}

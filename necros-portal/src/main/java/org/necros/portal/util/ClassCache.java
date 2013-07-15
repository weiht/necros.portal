/**
 * 
 */
package org.necros.portal.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author weiht
 *
 */
public class ClassCache {
	private static final Logger logger = LogManager.getLogger(ClassCache.class);
	
	private Map<String, Class<? extends Object>> cachedClasses = new HashMap<String, Class<? extends Object>>();

	public Class<? extends Object> get(String className) {
		Class<? extends Object> clazz = cachedClasses.get(className);
		if (clazz == null) {
			clazz = add(className);
		}
		return clazz;
	}

	private Class<? extends Object> add(String className) {
		Class<? extends Object> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (Exception ex) {
			logger.error(ex);
		}
		// 一次载入失败，则不再重复载入
		cachedClasses.put(className, clazz);
		return clazz;
	}
}

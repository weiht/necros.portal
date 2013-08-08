/**
 * 
 */
package org.necros.portal.conf;

/**
 * @author weiht
 *
 */
public interface SafeParamService {
	public abstract String stringValue(String key, String defaultValue);
	public abstract int intValue(String key, int defaultValue);
	public abstract long longValue(String key, long defaultValue);
	public abstract double floatValue(String key, double defaultValue);
	public abstract boolean boolValue(String key, boolean defaultValue);
}

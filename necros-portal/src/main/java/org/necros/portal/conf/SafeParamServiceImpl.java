/**
 * 
 */
package org.necros.portal.conf;

import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class SafeParamServiceImpl implements SafeParamService {
	private SysParamService sysParamService;

	@Override
	public String stringValue(String key, String defaultValue) {
		SysParam p = safeEval(key);
		if (p == null) return defaultValue;
		String v = p.getValue();
		return StringUtils.hasText(v) ? v : defaultValue;
	}

	private SysParam safeEval(String key) {
		try {
			return sysParamService.eval(key);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int intValue(String key, int defaultValue) {
		String v = stringValue(key, null);
		if (v == null) return defaultValue;
		try {
			return Integer.parseInt(v);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	public long longValue(String key, long defaultValue) {
		String v = stringValue(key, null);
		if (v == null) return defaultValue;
		try {
			return Long.parseLong(v);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	public double floatValue(String key, double defaultValue) {
		String v = stringValue(key, null);
		if (v == null) return defaultValue;
		try {
			return Double.parseDouble(v);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	@Override
	public boolean boolValue(String key, boolean defaultValue) {
		String v = stringValue(key, null);
		if (v == null) return defaultValue;
		return Boolean.parseBoolean(v);
	}

	public void setSysParamService(SysParamService sysParamService) {
		this.sysParamService = sysParamService;
	}
}

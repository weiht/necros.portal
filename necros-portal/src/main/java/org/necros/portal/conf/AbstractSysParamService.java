/**
 * 
 */
package org.necros.portal.conf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author weiht
 *
 */
public abstract class AbstractSysParamService implements SysParamService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractSysParamService.class);
	
	private Pattern placeholderPattern = Pattern.compile("(\\$\\{[\\w,/]+})");
	@Value("${hats.setting.baseKey:/}")
	private String baseKey;
	
	@Override
	public SysParam get(String key) {
		if (key == null) throw new RuntimeException("未指定系统参数的名称。");
		if (!key.startsWith(SysParam.splitter)) key = baseKey + key;
		return formatParam(doFindParamByKey(key));
	}
	
	protected abstract SysParam doFindParamByKey(String key);
	
	private SysParam formatParam(SysParam p) {
		if (p == null) return null;
		p.setValue(formatValue(p.getValue(), p.getKey()));
		return p;
	}
	
	private String formatValue(String sentence, String baseKey) {
		logger.debug("Formatting '{}', with value [{}]...", baseKey, sentence);
		String value = sentence;
		Matcher m = null;;
		while((m = placeholderPattern.matcher(value)).find()) {
			logger.debug("Matches: {}", m.groupCount());
			String g = m.group(1);
			String k = g.substring(2, g.length() - 1);
			String fk = k.startsWith(SysParam.splitter) ? k : baseKey.replaceAll("/([\\w])*$", "/" + k);
			SysParam p = get(fk);
			String v = null;
			if (p == null) {
				throw new RuntimeException("找不到名为“" + fk + "”的参数");
			} else {
				v = p.getValue();
			}
			logger.debug("parameter '{}' is [{}]", g, v);
			String replace = "(\\$\\{" + k + "})";
			logger.debug("Replacing [{}] with [{}]...", replace, v);
			value = value.replaceAll(replace, v);
			logger.debug("Param formatted to: [{}].", value);
		}
		return value;
	}
}

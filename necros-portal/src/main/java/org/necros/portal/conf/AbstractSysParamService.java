/**
 * 
 */
package org.necros.portal.conf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXB;

import org.necros.portal.conf.sysparamxsd.ObjectFactory;
import org.necros.portal.conf.sysparamxsd.SysParamType;
import org.necros.portal.conf.sysparamxsd.SysParamsType;
import org.necros.portal.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

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
	public SysParam eval(String key) {
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
	
	public String exportAll() throws IOException {
		ObjectFactory of = new ObjectFactory();
		SysParamsType spst = of.createSysParamsType();
		List<SysParamType> sptlst = spst.getSysParam();
		for (SysParam p: all()) {
			SysParamType pt = new SysParamType();
			pt.setKey(p.getKey());
			pt.setValue(p.getValue());
			pt.setDescription(p.getDescription());
			sptlst.add(pt);
		}
		File f = FileUtils.createTempFile();
		JAXB.marshal(of.createSysParams(spst), f);
		return f.getAbsolutePath();
	}
	
	public void importAll(String fn) throws IOException {
		SysParamsType spst = JAXB.unmarshal(new File(fn), SysParamsType.class);
		for (SysParamType pt: spst.getSysParam()) {
			String k = pt.getKey();
			if (StringUtils.hasText(k)) {
				SysParam p = get(k);
				if (p == null) {
					p = new SysParam();
					p.setKey(k);
					p.setValue(pt.getValue());
					p.setDescription(pt.getDescription());
					create(p);
				} else {
					p.setValue(pt.getValue());
					p.setDescription(pt.getDescription());
					update(p);
				}
			}
		}
		
	}
}

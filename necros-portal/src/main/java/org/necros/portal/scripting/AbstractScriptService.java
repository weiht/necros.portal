/**
 * 
 */
package org.necros.portal.scripting;

import java.util.List;

import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public abstract class AbstractScriptService implements ScriptService {
	private static final ScriptEngine unkownEngine;
	
	static {
		unkownEngine = new ScriptEngine();
		unkownEngine.setName("unkown");
	}
	
	protected List<ScriptEngine> engines;
	
	protected ScriptEngine parseEngine(String name) {
		if (!StringUtils.hasText(name))
			return unkownEngine;
		for (ScriptEngine se: engines) {
			List<String> exts = se.getExtensions();
			if (exts != null && !exts.isEmpty()) {
				for (String ext: exts) {
					if (name.endsWith(ext)) {
						return se;
					}
				}
			}
		}
		return unkownEngine;
	}

	public List<ScriptEngine> getEngines() {
		return engines;
	}

	public void setEngines(List<ScriptEngine> engines) {
		this.engines = engines;
	}
}

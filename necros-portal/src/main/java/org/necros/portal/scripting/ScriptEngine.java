/**
 * 
 */
package org.necros.portal.scripting;

import java.util.List;

/**
 * @author weiht
 *
 */
public class ScriptEngine {
	private String name;
	private List<String> extensions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
	}
}

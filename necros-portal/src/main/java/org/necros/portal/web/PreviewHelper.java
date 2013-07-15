/**
 * 
 */
package org.necros.portal.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class PreviewHelper {
	public Map<String, Object> makeObject() {
		return new HashMap<String, Object>();
	}
	
	public Map<String, Object> requestToObject(HttpServletRequest req) {
		Map<String, Object> r = makeObject();
		for (Entry<String, String[]> e: req.getParameterMap().entrySet()) {
			String k = e.getKey();
			String[] v = e.getValue();
			if (v != null)
				r.put(k, StringUtils.arrayToCommaDelimitedString(v));
		}
		return r;
	}
	
	public List<Object> makeList(Integer initSize) {
		if (initSize == null) return new ArrayList<Object>();
		return new ArrayList<Object>(initSize);
	}
	
	public String dateToStr(Date d, String pattern) {
		SimpleDateFormat f = new SimpleDateFormat(pattern);
		return f.format(d);
	}
	
	public String toDateMinutes(Date d) {
		return dateToStr(d, "yyyy-MM-dd HH:mm");
	}
	
	public String toDateStr(Date d) {
		return dateToStr(d, "yyyy-MM-dd");
	}
}

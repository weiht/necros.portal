package org.necros.pagination;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageHelper {
	private static final Logger logger = LoggerFactory.getLogger(PageHelper.class);
	
	public static final String DEF_PAGE_NUM_PARAM = "p";
	public static final String DEF_PAGE_SIZE_PARAM = "ps";
	
	private int pageSize = Pager.DEFAULT_PAGE_SIZE, listSize = Pager.DEFAUTL_LIST_SIZE;
	private String pageNumParam = DEF_PAGE_NUM_PARAM, pageSizeParam = DEF_PAGE_SIZE_PARAM;
	
	public Pager parseRequest(HttpServletRequest req) {
		Pager p = new Pager();
		p.setPageSize(parseIntParameter(req, pageSizeParam, pageSize));
		p.setListSize(listSize);
		p.setPageNum(parseIntParameter(req, pageNumParam, 1));
		logger.debug("{}", p);
		return p;
	}
	
	public String constructPageUrl(HttpServletRequest req, int pageNum) {
		StringBuilder buff = new StringBuilder();
		Enumeration<String> en = req.getParameterNames();
		while (en.hasMoreElements()) {
			String k = en.nextElement();
			if (!k.equals(pageNumParam)) {
				String v = req.getParameter(k);
				appendParam(buff, k, v);
			}
		}
		appendParam(buff, pageNumParam, Integer.toString(pageNum));
		return "?" + buff.toString();
	}

	private void appendParam(StringBuilder buff, String k, String v) {
		if (buff.length() > 0) buff.append('&');
		buff.append(k).append('=').append(v);
	}

	private int parseIntParameter(HttpServletRequest req,
			String paramName, int def) {
		String v = req.getParameter(paramName);
		try {
			return Integer.parseInt(v);
		} catch (Exception e) {
			return def;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	public String getPageNumParam() {
		return pageNumParam;
	}

	public void setPageNumParam(String pageNumParam) {
		this.pageNumParam = pageNumParam;
	}

	public String getPageSizeParam() {
		return pageSizeParam;
	}

	public void setPageSizeParam(String pageSizeParam) {
		this.pageSizeParam = pageSizeParam;
	}
}

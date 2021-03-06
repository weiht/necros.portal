/**
 * 
 */
package org.necros.pagination;

import java.util.List;

/**
 * @author weiht
 *
 */
public class PageQueryResult<T> {
	private Pager pager;
	private List<T> result;
	
	public PageQueryResult() {
	}
	
	public PageQueryResult(Pager p, List<T> r) {
		this.pager = p;
		this.result = r;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}
}

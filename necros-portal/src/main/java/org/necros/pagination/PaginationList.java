/**
 * 
 */
package org.necros.pagination;

import java.util.ArrayList;

/**
 * @author weiht
 *
 */
@SuppressWarnings("serial")
public class PaginationList<E> extends ArrayList<E> {
	private Pager pager;

	public Pager getPager() {
		if (pager == null) {
			pager = new Pager();
		}
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}
}

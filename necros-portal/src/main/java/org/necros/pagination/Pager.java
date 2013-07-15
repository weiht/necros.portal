/**
 * 
 */
package org.necros.pagination;

/**
 * @author weiht
 *
 */
public class Pager {
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final int DEFAUTL_LIST_SIZE = 10;
	public static final int MIN_PAGE_SIZE = 2;
	public static final int MIN_LIST_SIZE = 5;
	
	private int pageNum = 1;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int pageCount = 0;
	private int listSize = DEFAUTL_LIST_SIZE;
	private int listFirst;
	private int listLast;

	private int recordCount;

	public void calcPages() {
		if (recordCount > 0) {
			pageCount = (recordCount + pageSize - 1) / pageSize;
		}
		int diff = (listSize + 1) / 2;
		listFirst = pageNum - diff;
		if (listFirst < 1) listFirst = 1;
		listLast = listFirst + listSize - 1;
		diff = listLast - pageCount;
		if (diff > 0) {
			listLast = listLast - diff;
			listFirst = listFirst - diff;
			if (listFirst < 1) listFirst = 1;
		}
	}
	
	public int getListFirst() {
		return listFirst;
	}
	
	public int getListLast() {
		return listLast;
	}
	
	public int getQueryFirst() {
		return (pageNum - 1) * pageSize;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
		calcPages();
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount < 0 ? 0 : pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize < MIN_PAGE_SIZE ? MIN_PAGE_SIZE : pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum < 1 ? 1 : pageNum;
	}
	
	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize < MIN_LIST_SIZE ? MIN_LIST_SIZE : listSize;
	}
	
	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("class: ").append(Pager.class).append('\n');
		buff.append("pageNum").append(pageNum).append('\n');
		buff.append("pageSize: ").append(pageSize).append('\n');
		buff.append("pageCount: ").append(pageCount).append('\n');
		buff.append("listSize: ").append(listSize).append('\n');
		buff.append("listFirst: ").append(listFirst).append('\n');
		buff.append("listLast: ").append(listLast).append('\n');
		buff.append("recordCount: ").append(recordCount).append('\n');
		return buff.toString();
	}
}

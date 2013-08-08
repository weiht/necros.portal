package org.necros.portal.conf;

public interface EntryServiceFactory {
	public abstract EntryService get(String categoryId);
	public abstract EntryService remove(String categoryId);
}
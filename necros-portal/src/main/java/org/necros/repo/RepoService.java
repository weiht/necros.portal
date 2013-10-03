package org.necros.repo;

import java.io.IOException;

public interface RepoService {
	public abstract void saveEntitiesToRepo() throws IOException;
	public abstract void saveEntitiesToRepoFiltered(String filter) throws IOException;
	public abstract void loadEntitiesFromRepo() throws IOException;
	public abstract void loadEntitiesFromRepoFiltered(String filter) throws IOException;
	public abstract void commitChanges(String comment);
	public abstract void setRepoPath(String path);
	public abstract void setEntityName(String entityName);
}

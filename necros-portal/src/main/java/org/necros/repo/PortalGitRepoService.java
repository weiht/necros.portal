package org.necros.repo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Criteria;
import org.necros.portal.util.SessionFactoryHelper;
import org.springframework.util.StringUtils;

public class PortalGitRepoService implements RepoService {
	private static final String DEF_FILE_NAME_PROPERTY = "id";
	private static final String DEF_FILE_CONTENT_PROPERTY = "template";
	private String entityName;
	private SessionFactoryHelper sessionFactoryHelper;
	private String repoPath;
	private String fileNamePattern;
	private String fileContentPattern;
	
	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(entityName);
	}
	
	@SuppressWarnings("rawtypes")
	private List loadEntities() {
		return createCriteria()
				.list();
	}
	
	@SuppressWarnings("rawtypes")
	private List filterEntities(String filter) {
		throw new RuntimeException("Not implemented.");
	}

	private File ensureRepoPath() throws IOException {
		File f = new File(repoPath);
		if (!f.exists() && !f.mkdirs()) throw new IOException("You have no permission to write to the repository: " + f.getAbsolutePath());
		return f;
	}

	private String generateFileName(File root, Object e) throws IOException {
		String prop = StringUtils.hasText(fileNamePattern) ? fileNamePattern : DEF_FILE_NAME_PROPERTY;
		Object val;
		try {
			val = BeanUtils.getProperty(e, prop);
		} catch (IllegalAccessException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		} catch (InvocationTargetException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		} catch (NoSuchMethodException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		}
		return val.toString();
	}

	private void writeFile(FileWriter w, Object e) throws IOException {
		String prop = StringUtils.hasText(fileContentPattern) ? fileContentPattern : DEF_FILE_CONTENT_PROPERTY;
		Object val;
		try {
			val = BeanUtils.getProperty(e, prop);
		} catch (IllegalAccessException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		} catch (InvocationTargetException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		} catch (NoSuchMethodException e1) {
			throw new IOException("Cannot acquire file name.", e1);
		}
		w.write(val.toString());
	}

	private void saveEntityToFile(File root, Object e) throws IOException {
		String f = generateFileName(root, e);
		FileWriter w = new FileWriter(f);
		try {
			writeFile(w, e);
		} finally {
			w.close();
		}
	}

	private void doSaveEntitiesToRepo(@SuppressWarnings("rawtypes") List entities) throws IOException {
		if (entities == null || entities.isEmpty()) return;
		
		File f = ensureRepoPath();
		for (Object e: entities) {
			saveEntityToFile(f, e);
		}
	}

	public void saveEntitiesToRepo() throws IOException {
		@SuppressWarnings("rawtypes")
		List entities = loadEntities();
		doSaveEntitiesToRepo(entities);
		
		//TODO Marshal entities to xml file.
		// Override the whole existing xml file.
	}

	public void saveEntitiesToRepoFiltered(String filter) throws IOException {
		@SuppressWarnings("rawtypes")
		List entities = filterEntities(filter);
		doSaveEntitiesToRepo(entities);
		
		//TODO Marshal entities to xml file.
		// Insert into or update the xml elements only.
	}

	public void loadEntitiesFromRepo() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void loadEntitiesFromRepoFiltered(String filter) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void commitChanges(String comment) {
		// TODO Auto-generated method stub
		
	}

	public void setRepoPath(String path) {
		this.repoPath = path;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}

	public void setFileContentPattern(String fileContentPattern) {
		this.fileContentPattern = fileContentPattern;
	}
}

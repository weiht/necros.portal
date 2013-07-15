package org.necros.portal.section;

import java.io.IOException;
import java.util.List;

public interface SectionService {
	List<Section> sectionsOwnedBy(String ownerId);
	Section findById(String id);
	void save(Section section);
	void remove(String id);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

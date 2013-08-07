package org.necros.portal.section;

import java.io.IOException;
import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;

public interface SectionService {
	List<Section> sectionsOwnedBy(String ownerId);
	Section findById(String id);
	void save(Section section);
	void remove(String id);
	List<Section> all();
	int countAll();
	PageQueryResult<Section> pageAll(Pager pager);
	List<Section> filter(String filterText);
	int countFiltered(String filterText);
	PageQueryResult<Section> pageFiltered(Pager pager, String filterText);
	String exportAll() throws IOException;
	void importAll(String fn) throws IOException;
}

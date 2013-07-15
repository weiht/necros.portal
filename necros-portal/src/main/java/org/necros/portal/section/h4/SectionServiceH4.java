package org.necros.portal.section.h4;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXB;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.necros.portal.section.Section;
import org.necros.portal.section.SectionService;
import org.necros.portal.section.xsd.ObjectFactory;
import org.necros.portal.section.xsd.SectionType;
import org.necros.portal.section.xsd.SectionsType;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.necros.portal.util.ZipExportCallback;
import org.necros.portal.util.ZipExporter;
import org.necros.portal.util.ZipImportCallback;
import org.necros.portal.util.ZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class SectionServiceH4 implements SectionService {
	private static final String SECTIONS_XML_NAME = "sections.xml";
	private static final String HQL_FIND_OWNED_BY = "from Section where ownerId = ?";
	private static final String HQL_FIND_ORPHANS = "from Section where ifnull(ownerId, '') = ''";
	private static final Logger logger = LoggerFactory.getLogger(SectionServiceH4.class);
	
	private ZipExporter zipExporter;
	private ZipImporter zipImporter;
	private String sectionFileExtension;
	
	private SessionFactoryHelper sessionFactoryHelper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Section> sectionsOwnedBy(String ownerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding sections owned by: [" + ownerId + "]");
		}
		Criteria c = sessionFactoryHelper.createCriteria(Section.class);
		if (StringUtils.hasText(ownerId)) {
			c.add(Restrictions.eq("ownerId", ownerId));
		} else {
			c.add(Restrictions.eqOrIsNull("ownerId", ""));
		}
		return c.list();
	}

	@Override
	public Section findById(String id) {
		return (Section) sessionFactoryHelper.getSession().get(Section.class, id);
	}

	@Override
	public void save(Section section) {
		sessionFactoryHelper.getSession().saveOrUpdate(section);
	}

	@Override
	public void remove(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing channel: " + id);
		}
		Section sec = findById(id);
		if (sec != null) {
			sessionFactoryHelper.getSession().delete(sec);
		}
	}

	@Override
	public String exportAll() throws IOException {
		File f = FileUtils.createTempFile();
		String fn = zipExporter.exportZip(f.getAbsolutePath(), new ZipExportCallback() {
			@Override
			public void doZip(ZipOutputStream zos) throws IOException {
				List<Section> sections = sectionsOwnedBy(null);
				zipSectionXml(zos, sections);
				if (sections == null || sections.isEmpty()) throw new IOException("未找到任何组件。");
				for (Section sec: sections) {
					zipSections(zos, sec);
				}
			}
		});
		return fn;
	}

	protected void zipSectionXml(ZipOutputStream zos, List<Section> sections) throws IOException {
		ObjectFactory of = new ObjectFactory();
		SectionsType sst = of.createSectionsType();
		List<SectionType> sts = sst.getSection();
		for (Section sec: sections) {
			SectionType t = new SectionType();
			t.setId(sec.getId());
			t.setDisplayName(sec.getDisplayName());
			sts.add(t);
		}
		ZipEntry ze = new ZipEntry(SECTIONS_XML_NAME);
		zos.putNextEntry(ze);
		JAXB.marshal(of.createSections(sst), zos);
	}

	private void zipSections(ZipOutputStream zos, Section sec) throws IOException {
		ZipEntry ze = new ZipEntry(sec.getId() + sectionFileExtension);
		zos.putNextEntry(ze);
		String t = sec.getTemplate();
		if (t == null) t = "";
		zos.write(t.getBytes());
	}

	@Override
	public void importAll(String fn) throws IOException {
		logger.debug("fn:" + fn);
		zipImporter.importZip(fn, new ZipImportCallback() {
			@Override
			public boolean doUnzip(String name, ZipInputStream zis) throws IOException {
				BufferedReader r = new BufferedReader(new InputStreamReader(zis));
				if (SECTIONS_XML_NAME.equals(name)) {
					unzipSectionsXml(r);
				} else {
					if (name.endsWith(sectionFileExtension)) {
						name = name.substring(0, name.length() - sectionFileExtension.length());
					}
					logger.debug(name);
					StringBuilder buff = readToEnd(r);
					Section sec = findById(name);
					if (sec == null) {
						sec = new Section();
						sec.setId(name);
						sec.setDisplayName(name);
					}
					sec.setTemplate(buff.toString());
					save(sec);
				}
				return true;
			}
		});
	}

	private void unzipSectionsXml(BufferedReader r) throws IOException {
		StringBuilder buff = readToEnd(r);
		StringReader sr = new StringReader(buff.toString());
		SectionsType sst = JAXB.unmarshal(sr, SectionsType.class);
		List<SectionType> sts = sst.getSection();
		for (SectionType t: sts) {
			String id = t.getId();
			if (StringUtils.hasText(id)) {
				Section sec = findById(id);
				String n = t.getDisplayName();
				if (sec == null) {
					sec = new Section();
					sec.setId(id);
					sec.setDisplayName(StringUtils.hasText(n) ? n : id);
					save(sec);
				} else {
					if (StringUtils.hasText(n)) {
						sec.setDisplayName(n);
						save(sec);
					}
				}
			}
		}
	}

	private StringBuilder readToEnd(BufferedReader r)
			throws IOException {
		String l;
		StringBuilder buff = new StringBuilder();
		while ((l = r.readLine()) != null) {
			if (buff.length() > 0) buff.append("\n");
			buff.append(l);
		}
		return buff;
	}

	public ZipExporter getZipExporter() {
		return zipExporter;
	}

	public void setZipExporter(ZipExporter zipExporter) {
		this.zipExporter = zipExporter;
	}

	public ZipImporter getZipImporter() {
		return zipImporter;
	}

	public void setZipImporter(ZipImporter zipImporter) {
		this.zipImporter = zipImporter;
	}

	public String getSectionFileExtension() {
		return sectionFileExtension;
	}

	public void setSectionFileExtension(String sectionFileExtension) {
		this.sectionFileExtension = sectionFileExtension;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

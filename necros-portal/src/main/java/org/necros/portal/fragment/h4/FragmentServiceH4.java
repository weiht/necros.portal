package org.necros.portal.fragment.h4;

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
import org.necros.portal.fragment.Fragment;
import org.necros.portal.fragment.FragmentService;
import org.necros.portal.fragment.xsd.FragmentType;
import org.necros.portal.fragment.xsd.FragmentsType;
import org.necros.portal.fragment.xsd.ObjectFactory;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.necros.portal.util.ZipExportCallback;
import org.necros.portal.util.ZipExporter;
import org.necros.portal.util.ZipImportCallback;
import org.necros.portal.util.ZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FragmentServiceH4 implements FragmentService {
	private static final String FRAGMENTS_XML_NAME = "ajaxcalls.xml";
	private static final String HQL_FIND_OWNED_BY = "from Fragment where ownerId = ?";
	private static final String HQL_FIND_ORPHANS = "from Fragment where ifnull(ownerId, '') = ''";
	private static final Logger logger = LoggerFactory.getLogger(FragmentServiceH4.class);
	
	private ZipExporter zipExporter;
	private ZipImporter zipImporter;
	private String fragmentFileExtension;
	
	private SessionFactoryHelper sessionFactoryHelper;

	@SuppressWarnings("unchecked")
	@Override
	public List<Fragment> fragmentsOwnedBy(String ownerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding fragments owned by: [" + ownerId + "]");
		}
		Criteria c = sessionFactoryHelper.createCriteria(Fragment.class);
		if (StringUtils.hasText(ownerId)) {
			c.add(Restrictions.eq("ownerId", ownerId));
		} else {
			c.add(Restrictions.eqOrIsNull("ownerId", ""));
		}
		return c.list();
	}

	@Override
	public Fragment findById(String id) {
		return (Fragment) sessionFactoryHelper.getSession().get(Fragment.class, id);
	}

	@Override
	public void save(Fragment frag) {
		sessionFactoryHelper.getSession().saveOrUpdate(frag);
	}

	@Override
	public void remove(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing fragment: " + id);
		}
		Fragment frag = findById(id);
		if (frag != null) {
			sessionFactoryHelper.getSession().delete(frag);
		}
	}

	@Override
	public String exportAll() throws IOException {
		File f = FileUtils.createTempFile();
		String fn = zipExporter.exportZip(f.getAbsolutePath(), new ZipExportCallback() {
			@Override
			public void doZip(ZipOutputStream zos) throws IOException {
				List<Fragment> fragments = fragmentsOwnedBy(null);
				if (fragments == null || fragments.isEmpty()) throw new IOException("未找到任何页面。");
				zipFragmentXml(zos, fragments);
				for (Fragment frag: fragments) {
					zipFragment(zos, frag);
				}
			}
		});
		return fn;
	}

	private void zipFragment(ZipOutputStream zos, Fragment frag) throws IOException {
		ZipEntry ze = new ZipEntry(frag.getId() + fragmentFileExtension);
		zos.putNextEntry(ze);
		String t = frag.getTemplate();
		if (t == null) t = "";
		zos.write(t.getBytes());
	}

	private void zipFragmentXml(ZipOutputStream zos, List<Fragment> fragments) throws IOException {
		ObjectFactory of = new ObjectFactory();
		FragmentsType cst = of.createFragmentsType();
		List<FragmentType> cts = cst.getFragment();
		for (Fragment frag: fragments) {
			FragmentType c = of.createFragmentType();
			c.setId(frag.getId());
			c.setDisplayName(frag.getDisplayName());
			c.setResultType(frag.getResultType());
			cts.add(c);
		}
		ZipEntry ze = new ZipEntry(FRAGMENTS_XML_NAME);
		zos.putNextEntry(ze);
		JAXB.marshal(of.createFragments(cst), zos);
	}

	@Override
	public void importAll(String fn) throws IOException {
		logger.debug("fn:" + fn);
		zipImporter.importZip(fn, new ZipImportCallback() {
			@Override
			public boolean doUnzip(String name, ZipInputStream zis) throws IOException {
				BufferedReader r = new BufferedReader(new InputStreamReader(zis));
				if (FRAGMENTS_XML_NAME.equals(name)) {
					unzipFragmentsXml(r);
				} else {
					if (name.endsWith(fragmentFileExtension)) {
						name = name.substring(0, name.length() - fragmentFileExtension.length());
					}
					logger.debug(name);
					StringBuilder buff = readToEnd(r);
					Fragment frag = findById(name);
					if (frag == null) {
						frag = new Fragment();
						frag.setId(name);
						frag.setDisplayName(name);
					}
					frag.setTemplate(buff.toString());
					save(frag);
				}
				return true;
			}
		});
	}

	private StringBuilder readToEnd(BufferedReader r) throws IOException {
		String l;
		StringBuilder buff = new StringBuilder();
		while ((l = r.readLine()) != null) {
			if (buff.length() > 0) buff.append("\n");
			buff.append(l);
		}
		return buff;
	}

	private void unzipFragmentsXml(BufferedReader r) throws IOException {
		StringBuilder buff = readToEnd(r);
		StringReader sr = new StringReader(buff.toString());
		FragmentsType cst = JAXB.unmarshal(sr, FragmentsType.class);
		List<FragmentType> chtypes = cst.getFragment();
		for (FragmentType t: chtypes) {
			String id = t.getId();
			if (StringUtils.hasText(id)) {
				Fragment frag = findById(id);
				String n = t.getDisplayName();
				if (frag == null) {
					frag = new Fragment();
					frag.setId(id);
					frag.setDisplayName(StringUtils.hasText(n) ? n : id);
					frag.setResultType(t.getResultType());
					save(frag);
				} else {
					if (StringUtils.hasText(n)) {
						frag.setDisplayName(n);
						frag.setResultType(t.getResultType());
						save(frag);
					}
				}
			}
		}
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

	public String getFragmentFileExtension() {
		return fragmentFileExtension;
	}

	public void setFragmentFileExtension(String fragmentFileExtension) {
		this.fragmentFileExtension = fragmentFileExtension;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

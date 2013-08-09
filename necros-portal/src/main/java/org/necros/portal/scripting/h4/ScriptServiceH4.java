/**
 * 
 */
package org.necros.portal.scripting.h4;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXB;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.scripting.AbstractScriptService;
import org.necros.portal.scripting.ScriptService;
import org.necros.portal.scripting.ServerSideScript;
import org.necros.portal.scripting.xsd.ObjectFactory;
import org.necros.portal.scripting.xsd.ScriptType;
import org.necros.portal.scripting.xsd.ScriptsType;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.necros.portal.util.ZipExportCallback;
import org.necros.portal.util.ZipExporter;
import org.necros.portal.util.ZipImportCallback;
import org.necros.portal.util.ZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class ScriptServiceH4 extends AbstractScriptService {
	private static final Logger logger = LoggerFactory.getLogger(ScriptServiceH4.class);
	public static final Class<?> clazz = ServerSideScript.class;
	public static final String EXPORT_XML_NAME = "scripts.xml";
	
	private SessionFactoryHelper sessionFactoryHelper;
	private ZipExporter zipExporter;
	private ZipImporter zipImporter;

	@Override
	public ServerSideScript get(String name) {
		return (ServerSideScript) sessionFactoryHelper.getSession().get(clazz, name);
	}

	@Override
	public ServerSideScript create(ServerSideScript s) {
		sessionFactoryHelper.getSession().save(s);
		return s;
	}

	@Override
	public ServerSideScript update(ServerSideScript s) {
		sessionFactoryHelper.getSession().update(s);
		return s;
	}

	@Override
	public ServerSideScript remove(String name) {
		ServerSideScript s = get(name);
		if (s != null) sessionFactoryHelper.getSession().delete(s);
		return s;
	}
	
	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(clazz);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerSideScript> all() {
		return createCriteria()
				.list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<ServerSideScript> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}
	
	private Criteria createFilter(String filterText) {
		return createCriteria().add(Restrictions.or(
				Restrictions.like("name", filterText, MatchMode.ANYWHERE),
				Restrictions.like("displayName", filterText, MatchMode.ANYWHERE),
				Restrictions.like("description", filterText, MatchMode.ANYWHERE)
				));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServerSideScript> filter(String filterText) {
		return createFilter(filterText)
				.list();
	}

	@Override
	public int countFiltered(String filterText) {
		return sessionFactoryHelper.count(createFilter(filterText));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<ServerSideScript> pageFiltered(Pager p, String filterText) {
		p.setRecordCount(countFiltered(filterText));
		return sessionFactoryHelper.pageResult(createFilter(filterText), p);
	}

	@Override
	public String exportAll() throws IOException {
		File f = FileUtils.createTempFile();
		logger.debug("Exporting server side scripts to: {}", f);
		String fn = zipExporter.exportZip(f.getAbsolutePath(), new ZipExportCallback() {
			@Override
			public void doZip(ZipOutputStream zos) throws IOException {
				List<ServerSideScript> scripts = all();
				if (scripts == null || scripts.isEmpty()) throw new IOException("未找到任何脚本。");
				zipScriptsXml(zos, scripts);
				for (ServerSideScript s: scripts) {
					zipScript(zos, s);
				}
			}
		});
		return fn;
	}

	private void zipScriptsXml(ZipOutputStream zos,
			List<ServerSideScript> scripts) throws IOException {
		ObjectFactory of = new ObjectFactory();
		ScriptsType sst = of.createScriptsType();
		List<ScriptType> stlst = sst.getScript();
		for (ServerSideScript s: scripts) {
			ScriptType st = of.createScriptType();
			st.setName(s.getName());
			st.setEngine(s.getEngine());
			st.setDisplayName(s.getDisplayName());
			st.setDescription(s.getDescription());
			stlst.add(st);
		}
		ZipEntry ze = new ZipEntry(EXPORT_XML_NAME);
		zos.putNextEntry(ze);
		JAXB.marshal(of.createScripts(sst), zos);
	}

	private void zipScript(ZipOutputStream zos, ServerSideScript s)
			throws IOException {
		ZipEntry ze = new ZipEntry(s.getName());
		zos.putNextEntry(ze);
		String src = s.getSourceCode();
		if (src == null) src = "";
		zos.write(src.getBytes());
	}

	@Override
	public void importAll(String fn) throws IOException {
		logger.debug("Importing server side scripts from: {}", fn);
		zipImporter.importZip(fn, new ZipImportCallback() {
			@Override
			public boolean doUnzip(String name, ZipInputStream zis) throws IOException {
				BufferedReader r = new BufferedReader(new InputStreamReader(zis));
				if (EXPORT_XML_NAME.equals(name)) {
					unzipScriptsXml(r);
				} else {
					logger.debug("Unzipping script: {}", name);
					unzipScript(name, r);
				}
				return false;
			}
		});
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

	private void unzipScriptsXml(BufferedReader r) throws IOException {
		ScriptsType sst = JAXB.unmarshal(readToEnd(r).toString(), ScriptsType.class);
		List<ScriptType> stlst = sst.getScript();
		for (ScriptType st: stlst) {
			String name = st.getName();
			if (!StringUtils.hasText(name)) continue;
			ServerSideScript s = get(name);
			if (s == null) {
				s = new ServerSideScript();
				s.setName(name);
				attrsToProps(st, name, s);
				create(s);
			} else {
				attrsToProps(st, name, s);
				update(s);
			}
		}
	}

	private void attrsToProps(ScriptType st, String name, ServerSideScript s) {
		String v;
		v = st.getDisplayName();
		if (StringUtils.hasText(v)) s.setDisplayName(v);
		else s.setDisplayName(name);
		v = st.getEngine();
		if (StringUtils.hasText(v)) s.setEngine(v);
		else s.setEngine(parseEngine(name).getName());
		v = st.getDescription();
		if (StringUtils.hasText(v)) s.setDescription(v);
	}

	private void unzipScript(String name, BufferedReader r) throws IOException {
		ServerSideScript s = get(name);
		if (s == null) {
			s = new ServerSideScript();
			s.setName(name);
			s.setDisplayName(name);
			s.setEngine(parseEngine(name).getName());
			s.setSourceCode(readToEnd(r).toString());
			create(s);
		} else {
			s.setSourceCode(readToEnd(r).toString());
			update(s);
		}
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setZipExporter(ZipExporter zipExporter) {
		this.zipExporter = zipExporter;
	}

	public void setZipImporter(ZipImporter zipImporter) {
		this.zipImporter = zipImporter;
	}
}

package org.necros.portal.ajax.h4;

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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.necros.portal.ajax.AjaxCall;
import org.necros.portal.ajax.AjaxCallService;
import org.necros.portal.ajax.xsd.AjaxCallType;
import org.necros.portal.ajax.xsd.AjaxCallsType;
import org.necros.portal.ajax.xsd.ObjectFactory;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.necros.portal.util.ZipExportCallback;
import org.necros.portal.util.ZipExporter;
import org.necros.portal.util.ZipImportCallback;
import org.necros.portal.util.ZipImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class AjaxCallServiceH4 implements AjaxCallService {
	private static final String CALLS_XML_NAME = "ajaxcalls.xml";
	private static final Logger logger = LoggerFactory.getLogger(AjaxCallServiceH4.class);
	
	private SessionFactoryHelper sessionFactoryHelper;
	
	private ZipExporter zipExporter;
	private ZipImporter zipImporter;
	private String callFileExtension;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AjaxCall> callsOwnedBy(String ownerId) {
		if (logger.isDebugEnabled()) {
			logger.debug("Finding calls owned by: [" + ownerId + "]");
		}
		Criteria c = sessionFactoryHelper.createCriteria(AjaxCall.class);
		if (StringUtils.hasText(ownerId)) {
			c.add(Restrictions.eq("ownerId", ownerId));
		} else {
			c.add(Restrictions.eqOrIsNull("ownerId", ""));
		}
		return c.list();
	}

	@Override
	public AjaxCall findById(String id) {
		return (AjaxCall) sessionFactoryHelper.getSession().get(AjaxCall.class, id);
	}

	@Override
	public void save(AjaxCall call) {
		sessionFactoryHelper.getSession().saveOrUpdate(call);
	}

	@Override
	public void remove(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing call: " + id);
		}
		AjaxCall call = findById(id);
		if (call != null) {
			sessionFactoryHelper.getSession().delete(call);
		}
	}

	@Override
	public String exportAll() throws IOException {
		File f = FileUtils.createTempFile();
		String fn = zipExporter.exportZip(f.getAbsolutePath(), new ZipExportCallback() {
			@Override
			public void doZip(ZipOutputStream zos) throws IOException {
				List<AjaxCall> calls = callsOwnedBy(null);
				if (calls == null || calls.isEmpty()) throw new IOException("未找到任何页面。");
				zipCallXml(zos, calls);
				for (AjaxCall c: calls) {
					zipCall(zos, c);
				}
			}
		});
		return fn;
	}

	private void zipCall(ZipOutputStream zos, AjaxCall c) throws IOException {
		ZipEntry ze = new ZipEntry(c.getId() + callFileExtension);
		zos.putNextEntry(ze);
		String t = c.getTemplate();
		if (t == null) t = "";
		zos.write(t.getBytes());
	}

	private void zipCallXml(ZipOutputStream zos, List<AjaxCall> calls) throws IOException {
		ObjectFactory of = new ObjectFactory();
		AjaxCallsType cst = of.createAjaxCallsType();
		List<AjaxCallType> cts = cst.getAjaxCall();
		for (AjaxCall call: calls) {
			AjaxCallType c = of.createAjaxCallType();
			c.setId(call.getId());
			c.setDisplayName(call.getDisplayName());
			c.setResultType(call.getResultType());
			cts.add(c);
		}
		ZipEntry ze = new ZipEntry(CALLS_XML_NAME);
		zos.putNextEntry(ze);
		JAXB.marshal(of.createAjaxCalls(cst), zos);
	}

	@Override
	public void importAll(String fn) throws IOException {
		logger.debug("fn:" + fn);
		zipImporter.importZip(fn, new ZipImportCallback() {
			@Override
			public boolean doUnzip(String name, ZipInputStream zis) throws IOException {
				BufferedReader r = new BufferedReader(new InputStreamReader(zis));
				if (CALLS_XML_NAME.equals(name)) {
					unzipCallsXml(r);
				} else {
					if (name.endsWith(callFileExtension)) {
						name = name.substring(0, name.length() - callFileExtension.length());
					}
					logger.debug(name);
					StringBuilder buff = readToEnd(r);
					AjaxCall ch = findById(name);
					if (ch == null) {
						ch = new AjaxCall();
						ch.setId(name);
						ch.setDisplayName(name);
					}
					ch.setTemplate(buff.toString());
					save(ch);
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

	private void unzipCallsXml(BufferedReader r) throws IOException {
		StringBuilder buff = readToEnd(r);
		StringReader sr = new StringReader(buff.toString());
		AjaxCallsType cst = JAXB.unmarshal(sr, AjaxCallsType.class);
		List<AjaxCallType> chtypes = cst.getAjaxCall();
		for (AjaxCallType t: chtypes) {
			String id = t.getId();
			if (StringUtils.hasText(id)) {
				AjaxCall ch = findById(id);
				String n = t.getDisplayName();
				if (ch == null) {
					ch = new AjaxCall();
					ch.setId(id);
					ch.setDisplayName(StringUtils.hasText(n) ? n : id);
					ch.setResultType(t.getResultType());
					save(ch);
				} else {
					if (StringUtils.hasText(n)) {
						ch.setDisplayName(n);
						ch.setResultType(t.getResultType());
						save(ch);
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

	public String getCallFileExtension() {
		return callFileExtension;
	}

	public void setCallFileExtension(String callFileExtension) {
		this.callFileExtension = callFileExtension;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}
}

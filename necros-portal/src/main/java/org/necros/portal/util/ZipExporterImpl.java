package org.necros.portal.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class ZipExporterImpl implements ZipExporter {
	private static final Logger logger = LogManager.getLogger(ZipExporterImpl.class);
	
	@SuppressWarnings("unused")
	private Charset charset;
	
	@Override
	public String exportZip(String fn, ZipExportCallback callback) throws IOException {
		logger.debug("export to: " + fn);
		File f = null;
		if (!StringUtils.hasText(fn)) {
			f = FileUtils.createTempFile();
		} else {
			if((f = new File(fn)).exists() && !f.isFile())
				throw new IOException("无法导出文件，请检查路径设置。");
		}
		if (f.exists()) f.delete();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
		try {
			callback.doZip(zos);
		} finally {
			try {zos.close();} catch (Exception ex) {}
		}
		return f.getAbsolutePath();
	}

	@Override
	public void setCharset(String charsetName) {
		charset = Charset.forName(charsetName);
	}
}

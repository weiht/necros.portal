package org.necros.portal.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	private FileUtils() {}

	public static String combineFileName(String... parts) {
		StringBuilder buff = new StringBuilder();
		for (String p: parts) {
			if (!StringUtils.hasText(p)) continue;
			if (buff.length() > 0 && !p.startsWith(File.separator)) {
				buff.append(File.separator);
			}
			if (p.endsWith(File.separator)) {
				p = p.substring(0, p.length() - 1);
			}
			buff.append(p);
		}
		String fn = buff.length() > 0 ? buff.toString() : null;
		logger.debug(fn);
		return fn;
	}
	
	public static File createTempFile() throws IOException {
		return File.createTempFile("fu-", ".fu");
	}
	
	public static String toTempFile(InputStream ins) throws IOException {
		File f = createTempFile();
		logger.debug("{}", f);
		FileOutputStream fos = new FileOutputStream(f);
		try {
			int l = 0;
			byte[] buff = new byte[4096];
			while ((l = ins.read(buff)) > 0) {
				fos.write(buff, 0, l);
			}
		} finally {
			fos.close();
		}
		return f.getAbsolutePath();
	}
}

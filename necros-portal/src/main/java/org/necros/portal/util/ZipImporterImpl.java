/**
 * 
 */
package org.necros.portal.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weiht
 *
 */
public class ZipImporterImpl implements ZipImporter {
	private static final Logger logger = LoggerFactory.getLogger(ZipImporterImpl.class);

	@Override
	public void importZip(String fn, ZipImportCallback callback) throws IOException {
		logger.debug("importing from: " + fn);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(fn));
		try {
			ZipEntry e = null;
			while ((e = zis.getNextEntry()) != null) {
				if (!callback.doUnzip(e.getName(), zis)) break;
			}
		} finally {
			try {zis.close();} catch (IOException ex) {}
		}
	}
}

package org.necros.portal.util;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

public interface ZipExportCallback {
	void doZip(ZipOutputStream zos) throws IOException;
}

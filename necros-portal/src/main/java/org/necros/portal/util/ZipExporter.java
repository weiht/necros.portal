package org.necros.portal.util;

import java.io.IOException;

public interface ZipExporter {
	String exportZip(String fn, ZipExportCallback callback) throws IOException;
	void setCharset(String charsetName);
}

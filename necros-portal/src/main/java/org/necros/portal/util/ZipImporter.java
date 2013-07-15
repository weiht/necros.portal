package org.necros.portal.util;

import java.io.IOException;

public interface ZipImporter {
	void importZip(String fn, ZipImportCallback callback) throws IOException;
}

package org.necros.portal.util;

import java.io.IOException;
import java.util.zip.ZipInputStream;

public interface ZipImportCallback {
	boolean doUnzip(String name, ZipInputStream zis) throws IOException;
}

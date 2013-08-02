/**
 * 
 */
package org.necros.portal.util;

/**
 * @author weiht
 *
 */
public class StringUtils {
	private static final String HEX_STRING = "0123456789ABCDEF";
	private static final char[] HEX_CHARS = HEX_STRING.toCharArray();
	
	public static String bytesToString(byte[] src) {
		StringBuilder buff = new StringBuilder();
		for (byte b: src) {
			buff.append(HEX_CHARS[b >> 4]);
			buff.append(HEX_CHARS[b % 16]);
		}
		return buff.toString();
	}
	
	public static byte[] stringToBytes(String src) {
		String str = src.length() % 2 == 0 ? src : ("0" + src);
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i ++) {
			byte b;
			int ix;
			ix = HEX_STRING.indexOf(str.charAt(i * 2));
			if (ix < 0) throw new NumberFormatException(src);
			b = (byte) (ix << 4);
			ix = HEX_STRING.indexOf(str.charAt(i * 2 + 1));
			if (ix < 0) throw new NumberFormatException(src);
			b |= ix;
			bytes[i] = b;
		}
		return bytes;
	}
}

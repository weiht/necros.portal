/**
 * 
 */
package org.necros.portal.org;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.CharRange;

/**
 * @author weiht
 *
 */
public class CharRangePasswordGenerator implements PasswordGenerator {
	private static final CharRange POSSIBLE_PASSWORD_CHARS = new CharRange((char)33, (char)126);
	
	private List<CharRange> ranges;
	private char[] chars = null;
	private int passwordLength = 8;
	
	public String generate() {
		ensureChars();
		return doGenerate();
	}

	private String doGenerate() {
		int len = passwordLength;
		Random rnd = new Random();
		StringBuilder buff = new StringBuilder(len);
		for (int i = 0; i < len; i ++) {
			buff.append(chars[rnd.nextInt(chars.length)]);
		}
		return buff.toString();
	}

	private synchronized void ensureChars() {
		if (chars == null) {
			if (ranges == null || ranges.isEmpty()) {
				chars = POSSIBLE_PASSWORD_CHARS.toString().toCharArray();
			} else {
				StringBuilder buff = new StringBuilder();
				for (CharRange r: ranges) {
					buff.append(r.toString());
				}
				chars = new char[buff.length()];
				buff.getChars(0, buff.length(), chars, 0);
			}
		}
	}

	public List<CharRange> getRanges() {
		return ranges;
	}

	public void setRanges(List<CharRange> ranges) {
		this.ranges = ranges;
	}

	public int getPasswordLength() {
		return passwordLength;
	}

	public void setPasswordLength(int passwordLength) {
		this.passwordLength = passwordLength;
	}
}

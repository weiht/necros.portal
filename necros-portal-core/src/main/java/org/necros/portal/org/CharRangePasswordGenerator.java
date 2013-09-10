/**
 * 
 */
package org.necros.portal.org;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang.CharRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author weiht
 *
 */
public class CharRangePasswordGenerator implements PasswordGenerator {
	private static final Logger logger = LoggerFactory.getLogger(CharRangePasswordGenerator.class);
	private static final CharRange POSSIBLE_PASSWORD_CHARS = new CharRange((char)33, (char)126);
	
	private List<CharRange> ranges;
	private char[] chars = null;
	private int passwordLength = 8;
	
	public String generate() {
		ensureChars();
		logger.trace("Possible password chars: {}", chars);
		return doGenerate();
	}

	private String doGenerate() {
		int len = passwordLength;
		Random rnd = new Random();
		StringBuilder buff = new StringBuilder(len);
		for (int i = 0; i < len; i ++) {
			buff.append(chars[rnd.nextInt(chars.length)]);
		}
		logger.trace("Password generated: {}", buff);
		return buff.toString();
	}

	private synchronized void ensureChars() {
		StringBuilder buff = new StringBuilder();
		if (chars == null) {
			if (ranges == null || ranges.isEmpty()) {
				appendCharRange(buff, POSSIBLE_PASSWORD_CHARS);
			} else {
				for (CharRange r: ranges) {
					appendCharRange(buff, r);
				}
			}
		}
		chars = new char[buff.length()];
		buff.getChars(0, buff.length(), chars, 0);
	}
	
	private void appendCharRange(StringBuilder buff, CharRange r) {
		int start = r.getStart(), end = r.getEnd();
		for (char c = (char) start; c <= end; c ++) {
			buff.append(c);
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

/**
 * 
 */
package org.necros.portal.org;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.necros.portal.util.StringUtils;

/**
 * @author weiht
 *
 */
public class SaltMd5PasswordEncoder implements PasswordEncoder {
	private MessageDigest getMd5() {
		try {
			return MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public String encode(String passwd, String login, Person p) {
		String toEncode = passwd + login + p.getId();
		MessageDigest md5 = getMd5();
		byte[] d = md5.digest(toEncode.getBytes());
		return StringUtils.bytesToString(d);
	}
}

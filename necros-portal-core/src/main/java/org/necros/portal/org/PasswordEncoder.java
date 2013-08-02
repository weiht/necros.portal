/**
 * 
 */
package org.necros.portal.org;

/**
 * @author weiht
 *
 */
public interface PasswordEncoder {
	public abstract String encode(String passwd, String login, Person p);
}

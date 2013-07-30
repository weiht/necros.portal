/**
 * 
 */
package org.necros.portal.data;

import java.io.Serializable;

/**
 * @author weiht
 *
 */
public interface IdGenerator {
	public abstract Serializable generate();
}

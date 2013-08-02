/**
 * 
 */
package org.necros.portal.data.h4;

import java.sql.Timestamp;
import java.util.Calendar;

import org.necros.portal.data.BasicObject;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.ObjectTouchLog;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class BasicObjectServiceH4 implements BasicObjectService {
	private SessionFactoryHelper sessionFactoryHelper;
	
	protected BasicObject get(String id) {
		return (BasicObject) sessionFactoryHelper.getSession().get(BasicObject.class, id);
	}

	public BasicObject touch(String id, String entity,
			String personId, String personName) {
		BasicObject obj = get(id);
		if (obj == null) {
			obj = new BasicObject();
			obj.setEntity(entity);
			obj.setOwnerId(personId);
			obj.setOwnerName(personName);
			obj.setCreatePersonId(personId);
			obj.setCreatePersonName(personName);
			obj.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			doTouch(obj, personId, personName);
			sessionFactoryHelper.getSession().save(obj);
		} else {
			doTouch(obj, personId, personName);
			sessionFactoryHelper.getSession().update(obj);
		}
		return obj;
	}

	/**
	 * @param obj
	 * @param personId
	 * @param personName
	 */
	private void doTouch(BasicObject obj, String personId, String personName) {
		obj.setUpdatePersonId(personId);
		obj.setUpdatePersonName(personName);
		obj.setUpdateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		ObjectTouchLog log = new ObjectTouchLog();
		log.setObjectId(obj.getId());
		log.setEntity(obj.getEntity());
		log.setUpdatePersonId(personId);
		log.setUpdatePersonName(personName);
		log.setUpdateTime(obj.getUpdateTime());
		sessionFactoryHelper.getSession().save(log);
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

}

/**
 * 
 */
package org.necros.portal.org.h4;

import java.util.List;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.data.BasicObjectService;
import org.necros.portal.data.IdGenerator;
import org.necros.portal.org.PasswordEncoder;
import org.necros.portal.org.Person;
import org.necros.portal.org.PersonService;
import org.necros.portal.util.SessionFactoryHelper;

/**
 * @author weiht
 *
 */
public class PersonServiceH4 implements PersonService {
	private static final Class<?> clazz = Person.class;
	
	protected SessionFactoryHelper sessionFactoryHelper;
	protected BasicObjectService basicObjectService;
	protected IdGenerator idGenerator;
	protected PasswordEncoder passwordEncoder;

	private void doUpdate(Person p) {
		sessionFactoryHelper.getSession().update(p);
	}

	public Person get(String id) {
		return (Person) sessionFactoryHelper.getSession().get(clazz, id);
	}

	public Person create(Person p, Person editor) {
		String id = (String) idGenerator.generate();
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		p.setId(id);
		p.setLoginPassword(passwordEncoder.encode(p.getLoginPassword(), p.getLoginName(), p));
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	public Person update(Person p, Person editor) {
		String id = p.getId();
		Person orig = get(id);
		basicObjectService.touch(id, clazz.getName(),
				editor == null ? null : editor.getId(),
				editor == null ? null : editor.getInfo().getName());
		orig.setInfo(p.getInfo());
		orig.setContact(p.getContact());
		orig.setDisplayOrder(p.getDisplayOrder());
		doUpdate(orig);
		return orig;
	}

	public Person remove(String id, Person editor) {
		Person p = get(id);
		if (p != null) {
			basicObjectService.touch(id, clazz.getName(),
					editor == null ? null : editor.getId(),
					editor == null ? null : editor.getInfo().getName());
			sessionFactoryHelper.getSession().delete(p);
		}
		return p;
	}

	public void changePassword(String id, String oldPwd, String newPwd) {
		Person p = get(id);
		String op = passwordEncoder.encode(oldPwd, p.getLoginName(), p);
		if (op.equals(p.getLoginPassword())) {
			p.setLoginPassword(passwordEncoder.encode(newPwd, p.getLoginName(), p));
		}
		doUpdate(p);
	}

	public String resetPassword(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#all()
	 */
	public List<Person> all() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#countAll()
	 */
	public int countAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#pageAll()
	 */
	public PageQueryResult<Person> pageAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#filter(java.lang.String)
	 */
	public List<Person> filter(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#countFiltered()
	 */
	public int countFiltered() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.necros.portal.org.PersonService#pageFiltered(org.necros.pagination.Pager, java.lang.String)
	 */
	public PageQueryResult<Person> pageFiltered(Pager p, String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setBasicObjectService(BasicObjectService basicObjectService) {
		this.basicObjectService = basicObjectService;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}

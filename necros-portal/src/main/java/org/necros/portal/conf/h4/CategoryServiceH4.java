/**
 * 
 */
package org.necros.portal.conf.h4;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXB;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.conf.CategoryService;
import org.necros.portal.conf.DictCategory;
import org.necros.portal.conf.DictEntry;
import org.necros.portal.conf.EntryService;
import org.necros.portal.conf.EntryServiceFactory;
import org.necros.portal.conf.dictxsd.CategoryType;
import org.necros.portal.conf.dictxsd.DictionaryType;
import org.necros.portal.conf.dictxsd.EntriesType;
import org.necros.portal.conf.dictxsd.EntryType;
import org.necros.portal.conf.dictxsd.ObjectFactory;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.SessionFactoryHelper;
import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class CategoryServiceH4 implements CategoryService {
	private SessionFactoryHelper sessionFactoryHelper;
	private EntryServiceFactory entryServiceFactory;
	
	@Override
	public DictCategory get(String key) {
		return (DictCategory) sessionFactoryHelper.getSession().get(DictCategory.class, key);
	}
	
	@Override
	public DictCategory create(DictCategory p) {
		sessionFactoryHelper.getSession().save(p);
		return p;
	}

	@Override
	public DictCategory update(DictCategory p) {
		sessionFactoryHelper.getSession().update(p);
		return p;
	}

	@Override
	public DictCategory remove(String key) {
		DictCategory p = get(key);
		sessionFactoryHelper.getSession().delete(p);
		entryServiceFactory.get(p.getName()).removeAll();
		entryServiceFactory.remove(key);
		return p;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictCategory> all() {
		return createCriteria().list();
	}

	@Override
	public int countAll() {
		return sessionFactoryHelper.count(createCriteria());
	}

	private Criteria createCriteria() {
		return sessionFactoryHelper.createCriteria(DictCategory.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<DictCategory> pageAll(Pager p) {
		p.setRecordCount(countAll());
		return sessionFactoryHelper.pageResult(createCriteria(), p);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DictCategory> filter(String filter) {
		return createFilter(filter).list();
	}

	private Criteria createFilter(String filter) {
		return createCriteria()
				.add(Restrictions.or(Restrictions.like("name", filter, MatchMode.ANYWHERE),
						Restrictions.like("displayName", filter, MatchMode.ANYWHERE),
						Restrictions.like("description", filter, MatchMode.ANYWHERE)));
	}

	@Override
	public int countFiltered(String filter) {
		return sessionFactoryHelper.count(createFilter(filter));
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageQueryResult<DictCategory> pageFiltered(Pager p, String filter) {
		p.setRecordCount(countFiltered(filter));
		return sessionFactoryHelper.pageResult(createFilter(filter), p);
	}

	/**
	 * @param ct
	 */
	private void exportEntries(CategoryType ct) {
		ct.setEntries(new EntriesType());
		List<EntryType> ets = ct.getEntries().getEntry();
		for (DictEntry e: entryServiceFactory.get(ct.getName()).all()) {
			EntryType et = new EntryType();
			et.setCategoryId(e.getCategoryId());
			et.setKey(e.getKey());
			et.setDisplayText(e.getDisplayText());
			et.setDescription(e.getDescription());
			et.setDisplayOrder(e.getDisplayOrder());
			ets.add(et);
		}
	}

	@Override
	public String exportAll() throws IOException {
		ObjectFactory of = new ObjectFactory();
		DictionaryType dt = of.createDictionaryType();
		List<CategoryType> cts = dt.getCategory();
		for (DictCategory c: all()) {
			CategoryType ct = of.createCategoryType();
			ct.setName(c.getName());
			ct.setDisplayName(c.getDisplayName());
			ct.setDescription(c.getDescription());
			exportEntries(ct);
			cts.add(ct);
		}
		File f = FileUtils.createTempFile();
		JAXB.marshal(of.createDictionary(dt), f);
		return f.getAbsolutePath();
	}

	/**
	 * @param ct
	 */
	private void importEntries(CategoryType ct) {
		EntryService esvc = entryServiceFactory.get(ct.getName());
		for (EntryType et: ct.getEntries().getEntry()) {
			DictEntry e = esvc.get(et.getKey());
			if (e == null) {
				e = new DictEntry();
				e.setCategoryId(ct.getName());
				e.setKey(et.getKey());
				e.setDisplayText(et.getDisplayText());
				e.setDescription(et.getDescription());
				e.setDisplayOrder(e.getDisplayOrder());
				esvc.create(e);
			} else {
				String t = et.getDisplayText();
				if (StringUtils.hasText(t)) {
					e.setDisplayText(t);
				}
				e.setDisplayText(et.getDisplayText());
				e.setDescription(et.getDescription());
				e.setDisplayOrder(e.getDisplayOrder());
				esvc.update(e);
			}
		}
	}

	@Override
	public void importAll(String fn) throws IOException {
		DictionaryType dt = JAXB.unmarshal(new File(fn), DictionaryType.class);
		for (CategoryType ct: dt.getCategory()) {
			String n = ct.getName();
			if (StringUtils.hasText(n)) {
				DictCategory c = get(n);
				if (c == null) {
					c = new DictCategory();
					c.setName(n);
					c.setDisplayName(ct.getDisplayName());
					c.setDescription(ct.getDescription());
					create(c);
				} else {
					String dn = ct.getDisplayName();
					if (StringUtils.hasText(dn)) {
						c.setDisplayName(dn);
					}
					c.setDescription(ct.getDescription());
					update(c);
				}
				importEntries(ct);
			}
		}
	}

	public void setSessionFactoryHelper(SessionFactoryHelper sessionFactoryHelper) {
		this.sessionFactoryHelper = sessionFactoryHelper;
	}

	public void setEntryServiceFactory(EntryServiceFactory entryServiceFactory) {
		this.entryServiceFactory = entryServiceFactory;
	}
}

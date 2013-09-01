/**
 * 
 */
package org.necros.portal.data.web;

import java.util.List;
import java.util.Map;

import org.necros.pagination.PageQueryResult;
import org.necros.pagination.Pager;
import org.necros.portal.data.GeneralDao;
import org.necros.portal.data.GeneralProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author weiht
 *
 */
@Controller @RequestMapping(value=HibernateDataAccessController.BASE_URL)
public class HibernateDataAccessController {
	public static final Logger logger = LoggerFactory.getLogger(HibernateDataAccessController.class);
	public static final String BASE_URL = "/data";
	public static final String CRUD_URL = "/d";
	public static final String QUERY_URL = "/l";
	
	@Resource(name="d.dao")
	private GeneralDao dao;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=CRUD_URL + "/{entity}/{id}", method=RequestMethod.GET)
	public @ResponseBody Map get(@PathVariable("entity") String entity, @PathVariable("id") String id) {
		return dao.get(entity, id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=CRUD_URL + "/{entity}", method=RequestMethod.POST)
	public @ResponseBody Map create(@PathVariable("entity") String entity, @RequestBody Map obj) {
		return dao.create(entity, obj);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value=CRUD_URL + "/{entity}/{id}", method=RequestMethod.POST)
	public @ResponseBody Map update(@PathVariable("entity") String entity, @PathVariable("id") String id
			, @RequestBody Map obj) {
		obj.put(GeneralProperties.PROP_ID, id);
		return dao.update(entity, obj);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=CRUD_URL + "/{entity}/{id}", method=RequestMethod.DELETE)
	public @ResponseBody Map remove(@PathVariable("entity") String entity, @PathVariable("id") String id) {
		return dao.remove(entity, id);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=QUERY_URL + "/{entity}", method=RequestMethod.GET)
	public @ResponseBody List list(@PathVariable("entity") String entity) {
		return dao.all(entity);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value=QUERY_URL + "/{entity}/{pn}", method=RequestMethod.GET)
	public @ResponseBody PageQueryResult page(@PathVariable("entity") String entity, @PathVariable("pn") int pn) {
		Pager p = new Pager();
		p.setPageNum(pn);
		return dao.pageAll(entity, p);
	}
}

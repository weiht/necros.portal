package org.necros.portal.org.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.org.Person;
import org.necros.portal.org.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonController {
	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	
	public static final String BASE_CTX_URL = "/person";
	
	@Resource(name="c.personService")
	private PersonService personService;

	@RequestMapping(value=BASE_CTX_URL + "/all", method=RequestMethod.GET)
	public @ResponseBody List<Person> allPersons(HttpServletRequest req) {
		return personService.all();
	}

	@RequestMapping(value=BASE_CTX_URL + "/get/{id}", method=RequestMethod.GET)
	public @ResponseBody Person getPerson(@PathVariable String id, HttpServletRequest req) {
		return personService.get(id);
	}

	@RequestMapping(value=BASE_CTX_URL + "/add", method=RequestMethod.POST)
	public @ResponseBody Person addPerson(@RequestBody Person p, HttpServletRequest req) {
		return personService.create(p, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/update", method=RequestMethod.POST)
	public @ResponseBody Person updatePerson(@RequestBody Person p, HttpServletRequest req) {
		return personService.update(p, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/remove", method=RequestMethod.POST)
	public @ResponseBody Person removePerson(@RequestBody Person p, HttpServletRequest req) {
		return personService.remove(p.getId(), SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/resetPassword/{id}", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> resetPassword(@PathVariable String id, HttpServletRequest req) {
		String pwd = personService.resetPassword(id, SessionContext.getCurrentContext(req).getCurrentUser());
		logger.debug("用户{}的口令已重置为：{}", id, pwd);
		Map<String, String> result = new HashMap<String, String>();
		result.put("password", pwd);
		return result;
	}
}

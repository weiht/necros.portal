package org.necros.portal.org.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.org.Person;
import org.necros.portal.org.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonController {
	public static final String BASE_CTX_URL = "/person";
	
	@Resource(name="c.personService")
	private PersonService personService;

	@RequestMapping(value=BASE_CTX_URL + "/all", method=RequestMethod.GET)
	public @ResponseBody List<Person> allPersons(@PathVariable String id, HttpServletRequest req) {
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
}

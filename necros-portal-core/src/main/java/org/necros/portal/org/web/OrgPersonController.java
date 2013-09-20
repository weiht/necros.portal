package org.necros.portal.org.web;

import java.util.List;

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
public class OrgPersonController {
	private static final Logger logger = LoggerFactory.getLogger(OrgPersonController.class);
	
	public static final String BASE_CTX_URL = "/orgmembers";

	@Resource(name="c.personService")
	private PersonService personService;

	@RequestMapping(value=BASE_CTX_URL + "/direct/{orgId}", method=RequestMethod.GET)
	public @ResponseBody List<Person> directMembers(@PathVariable String orgId, HttpServletRequest req) {
		return personService.orgAllDirectMembers(orgId);
	}

	@RequestMapping(value=BASE_CTX_URL + "/decend/{orgId}", method=RequestMethod.GET)
	public @ResponseBody List<Person> decendMembers(@PathVariable String orgId, HttpServletRequest req) {
		return personService.orgAllDescendMembers(orgId);
	}

	@RequestMapping(value=BASE_CTX_URL + "/update/{orgId}", method=RequestMethod.POST)
	public @ResponseBody Person updateMember(@PathVariable String orgId, @RequestBody Person p, HttpServletRequest req) {
		Person editor = SessionContext.getCurrentContext(req).getCurrentUser();
		logger.debug("Updating person {} to org {}.", p.getId(), orgId);
		Person updated = personService.updateMembership(p, orgId, editor);
		logger.debug("Org id: {}, org path: {}", updated.getOrgId(), updated.getOrgPath());
		return updated;
	}

	@RequestMapping(value=BASE_CTX_URL + "/update", method=RequestMethod.POST)
	public @ResponseBody Person removeMember(@RequestBody Person p, HttpServletRequest req) {
		return updateMember(null, p, req);
	}
}

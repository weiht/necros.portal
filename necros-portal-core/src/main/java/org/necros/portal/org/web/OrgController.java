package org.necros.portal.org.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.org.Organization;
import org.necros.portal.org.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrgController {
	public static final String BASE_CTX_URL = "/org";
	
	@Resource(name="c.menuService")
	private OrganizationService orgService;

	@RequestMapping(value=BASE_CTX_URL + "/children/:id", method=RequestMethod.GET)
	public @ResponseBody List<Organization> listChildren(@PathVariable String id, HttpServletRequest req) {
		return orgService.children(id);
	}

	@RequestMapping(value=BASE_CTX_URL + "/get/:id", method=RequestMethod.GET)
	public @ResponseBody Organization getOrg(@PathVariable String id, HttpServletRequest req) {
		return orgService.get(id);
	}

	@RequestMapping(value=BASE_CTX_URL + "/add", method=RequestMethod.POST)
	public @ResponseBody Organization addOrg(@RequestBody Organization org, HttpServletRequest req) {
		return orgService.create(org, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/update", method=RequestMethod.POST)
	public @ResponseBody Organization updateOrg(@RequestBody Organization org, HttpServletRequest req) {
		return orgService.update(org, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/remove", method=RequestMethod.POST)
	public @ResponseBody Organization removeOrg(@RequestBody Organization org, HttpServletRequest req) {
		return orgService.remove(org.getId(), SessionContext.getCurrentContext(req).getCurrentUser());
	}
}

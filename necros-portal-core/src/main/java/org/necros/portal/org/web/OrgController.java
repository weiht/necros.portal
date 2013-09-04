package org.necros.portal.org.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.org.Organization;
import org.necros.portal.org.OrganizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrgController {
	public static final String BASE_CTX_URL = "/org";
	
	@Resource(name="c.menuService")
	private OrganizationService orgService;

	@RequestMapping(value=BASE_CTX_URL + "/add", method=RequestMethod.POST)
	public @ResponseBody Organization addOrg(@RequestBody Organization itm, HttpServletRequest req) {
		return orgService.create(itm, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/update", method=RequestMethod.POST)
	public @ResponseBody Organization updateOrg(@RequestBody Organization itm, HttpServletRequest req) {
		return orgService.update(itm, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/remove", method=RequestMethod.POST)
	public @ResponseBody Organization removeOrg(@RequestBody Organization itm, HttpServletRequest req) {
		return orgService.remove(itm.getId(), SessionContext.getCurrentContext(req).getCurrentUser());
	}
}

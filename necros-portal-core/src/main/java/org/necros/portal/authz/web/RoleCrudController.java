package org.necros.portal.authz.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.authz.Role;
import org.necros.portal.authz.RoleCrudService;
import org.necros.portal.org.web.SessionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weiht
 *
 */
@Controller
public class RoleCrudController {
	public static final String BASE_CTX_URL = "/role";

	@Resource(name="c.roleCrudService")
	private RoleCrudService roleCrudService;

	@RequestMapping(value=BASE_CTX_URL + "/all", method=RequestMethod.GET)
	public @ResponseBody List<Role> allPersons(HttpServletRequest req) {
		return roleCrudService.all();
	}

	@RequestMapping(value=BASE_CTX_URL + "/get/{id}", method=RequestMethod.GET)
	public @ResponseBody Role getPerson(@PathVariable String id, HttpServletRequest req) {
		return roleCrudService.get(id);
	}

	@RequestMapping(value=BASE_CTX_URL + "/add", method=RequestMethod.POST)
	public @ResponseBody Role addPerson(@RequestBody Role r, HttpServletRequest req) {
		return roleCrudService.create(r, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/update", method=RequestMethod.POST)
	public @ResponseBody Role updatePerson(@RequestBody Role r, HttpServletRequest req) {
		return roleCrudService.update(r, SessionContext.getCurrentContext(req).getCurrentUser());
	}

	@RequestMapping(value=BASE_CTX_URL + "/remove", method=RequestMethod.POST)
	public @ResponseBody Role removePerson(@RequestBody Role r, HttpServletRequest req) {
		return roleCrudService.remove(r.getId(), SessionContext.getCurrentContext(req).getCurrentUser());
	}
}

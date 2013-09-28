package org.necros.portal.authz.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.necros.portal.authz.RoleMember;
import org.necros.portal.authz.RoleMemberService;
import org.necros.portal.org.Person;
import org.necros.portal.org.web.SessionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weiht
 *
 */
@Controller
public class RoleMemberController {
	public static final String BASE_CTX_URL = "/rolemember";

	@Resource(name="c.roleMemberService")
	private RoleMemberService roleMemberService;

	@RequestMapping(value=BASE_CTX_URL + "/all/{roleId}", method=RequestMethod.GET)
	public @ResponseBody List<Person> allMembers(
			@PathVariable String roleId, HttpServletRequest req) {
		return roleMemberService.allMembers(roleId);
	}
	
	@RequestMapping(value=BASE_CTX_URL + "/add/{roleId}/{personId}", method=RequestMethod.POST)
	public @ResponseBody RoleMember addMember(@PathVariable("roleId") String roleId,
			@PathVariable("personId") String personId, HttpServletRequest req) {
		return roleMemberService.addMember(roleId, personId, SessionContext.getCurrentContext(req).getCurrentUser());
	}
	
	@RequestMapping(value=BASE_CTX_URL + "/remove/{roleId}/{personId}", method=RequestMethod.POST)
	public @ResponseBody RoleMember removeMember(@PathVariable("roleId") String roleId,
			@PathVariable("personId") String personId, HttpServletRequest req) {
		return roleMemberService.remove(roleId, personId, SessionContext.getCurrentContext(req).getCurrentUser());
	}
}

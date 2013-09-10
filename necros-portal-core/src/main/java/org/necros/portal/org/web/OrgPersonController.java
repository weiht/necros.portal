package org.necros.portal.org.web;

import javax.annotation.Resource;

import org.necros.portal.org.PersonService;
import org.springframework.stereotype.Controller;

@Controller
public class OrgPersonController {
	public static final String BASE_CTX_URL = "/orgpersons";

	@Resource(name="c.personService")
	private PersonService personService;
}

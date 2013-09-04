package org.necros.portal.org.web;

import javax.annotation.Resource;

import org.necros.portal.org.PersonService;
import org.springframework.stereotype.Controller;

@Controller
public class PersonController {
	public static final String BASE_CTX_URL = "/person";
	
	@Resource(name="c.menuService")
	private PersonService personService;
}

/**
 * 
 */
package org.necros.portal.menu.web;

import java.util.List;

import javax.annotation.Resource;

import org.necros.portal.menu.MenuItem;
import org.necros.portal.menu.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author weiht
 *
 */
@Controller(MenuController.BASE_CTX_URL)
public class MenuController {
	public static final String BASE_CTX_URL = "/menu";
	
	@Resource(name="c.menuService")
	private MenuService menuService;
	
	@RequestMapping(value="/display/{id}", method=RequestMethod.GET)
	public @ResponseBody List<MenuItem> displayMenu(@PathVariable("id") String id) {
		return menuService.display(id);
	}
}

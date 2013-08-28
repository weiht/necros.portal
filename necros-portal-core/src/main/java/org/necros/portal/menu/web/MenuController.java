/**
 * 
 */
package org.necros.portal.menu.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Controller
public class MenuController {
	public static final String BASE_CTX_URL = "/menu";
	
	@Resource(name="c.menuService")
	private MenuService menuService;
	
	@RequestMapping(value=BASE_CTX_URL + "/display/{id}", method=RequestMethod.GET)
	public @ResponseBody List<MenuItem> displayMenu(@PathVariable("id") String id) {
		return menuService.display(id);
	}

	@RequestMapping(value=BASE_CTX_URL + "/menus", method=RequestMethod.GET)
	public @ResponseBody List<MenuItem> menus() {
		return menuService.allRoot();
	}

	@RequestMapping(value=BASE_CTX_URL + "/editor/{id}", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> editor(@PathVariable("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		MenuItem menu = menuService.get(id);
		if (menu != null) {
			result.put("menuItem", menu);
			result.put("children", menuService.allChildren(id));
			result.put("breadcrumb", menuService.pathToRoot(menu));
		}
		return result;
	}
}

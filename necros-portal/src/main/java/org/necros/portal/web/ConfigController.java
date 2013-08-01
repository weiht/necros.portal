/**
 * 
 */
package org.necros.portal.web;

import org.necros.portal.conf.CategoryService;
import org.necros.portal.conf.DictCategory;
import org.necros.portal.conf.SysParam;
import org.necros.portal.conf.SysParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author weiht
 *
 */
@Controller
public class ConfigController {
	private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
	
	@Resource(name="p.sysParamService")
	private SysParamService sysParamService;
	@Resource(name="p.categoryService")
	private CategoryService categoryService;
	
	@RequestMapping(value="/html/action/save-sysparam", method=RequestMethod.POST)
	public @ResponseBody String saveSysParam(
			@ModelAttribute("sysparam") SysParam p,
			@RequestParam("action") String act) {
		try {
			if ("new".equalsIgnoreCase(act)) {
				logger.debug("Adding new sysparam: {}", p);
				sysParamService.create(p);
			} else {
				logger.debug("Updating sysparam: {}", p);
				sysParamService.update(p);
			}
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-sysparam", method=RequestMethod.POST)
	public @ResponseBody String removeSysParam(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				sysParamService.remove(id);
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/save-category", method=RequestMethod.POST)
	public @ResponseBody String saveCategory(
			@ModelAttribute("category") DictCategory cat,
			@RequestParam("action") String act) {
		try {
			if ("new".equalsIgnoreCase(act)) {
				logger.debug("Adding new category: {}", cat);
				categoryService.create(cat);
			} else {
				logger.debug("Updating category: {}", cat);
				categoryService.update(cat);
			}
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-category", method=RequestMethod.POST)
	public @ResponseBody String removeCategory(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				categoryService.remove(id);
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
}

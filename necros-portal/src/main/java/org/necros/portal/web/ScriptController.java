/**
 * 
 */
package org.necros.portal.web;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.necros.portal.scripting.ScriptService;
import org.necros.portal.scripting.ServerSideScript;
import org.necros.portal.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author weiht
 *
 */
@Controller
public class ScriptController {
	private static final Logger logger = LoggerFactory.getLogger(ScriptController.class);
	
	public static final String BASE_CTX_URL = "/html/action";
	
	@Resource(name="p.scriptService")
	private ScriptService scriptService;
	
	@RequestMapping(value=BASE_CTX_URL + "/save-script", method=RequestMethod.POST)
	public @ResponseBody String saveScript(
			@ModelAttribute ServerSideScript s,
			@RequestParam("action") String act) {
		try {
			if ("new".equalsIgnoreCase(act)) {
				logger.debug("Adding new server side script: {}", s);
				scriptService.create(s);
			} else {
				logger.debug("Updating server side script: {}", s);
				scriptService.update(s);
			}
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value=BASE_CTX_URL + "/del-script", method=RequestMethod.POST)
	public @ResponseBody String removeScript(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				scriptService.remove(id);
			return PageController.MSG_ACTION_OK;
		} catch (Exception ex) {
			return PageController.translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value=BASE_CTX_URL + "/export-scripts")
	public void exportAllScripts(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = scriptService.exportAll();
		String downloadName = "scripts.zip";
		if (logger.isDebugEnabled()) {
			logger.debug("Downloading exported scripts as: " + fn);
		}
		PageController.downloadFile(resp, fn, downloadName);
	}
	
	@RequestMapping(value=BASE_CTX_URL + "/import-scripts", method=RequestMethod.POST)
	public @ResponseBody String importScripts(@RequestParam("importFile") MultipartFile file)
			throws IOException {
		logger.debug("Scripts file uploaded: {}", file.getOriginalFilename());
		InputStream ins = file.getInputStream();
		try {
			scriptService.importAll(FileUtils.toTempFile(ins));
		} finally {
			ins.close();
		}
		return PageController.MSG_JSON_OK;
	}
}

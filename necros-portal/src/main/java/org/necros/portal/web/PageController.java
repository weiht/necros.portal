package org.necros.portal.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.necros.portal.ajax.AjaxCall;
import org.necros.portal.ajax.AjaxCallService;
import org.necros.portal.channel.Channel;
import org.necros.portal.channel.ChannelService;
import org.necros.portal.fragment.Fragment;
import org.necros.portal.fragment.FragmentService;
import org.necros.portal.section.Section;
import org.necros.portal.section.SectionService;
import org.necros.portal.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PageController {
	private static final String MSG_ACTION_OK = "OK";
	private static final String MSG_JSON_OK = "{}";

	private static final Logger logger = LoggerFactory.getLogger(PageController.class);

	private static final String JAR_CONTENT_TYPE = "application/octet-stream";
	
	@Resource(name="p.pageService")
	private PageService pageService;
	@Resource(name="p.channelService")
	private ChannelService channelService;
	@Resource(name="p.sectionService")
	private SectionService sectionService;
	@Resource(name="p.ajaxCallService")
	private AjaxCallService callService;
	@Resource(name="p.fragmentService")
	private FragmentService fragmentService;
	
	@RequestMapping("/html/{page}")
	public void html(@PathVariable("page") String page,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		pageService.renderPage(page, req, resp);
	}
	
	@RequestMapping("/preview/{id}")
	public void preview(@ModelAttribute("channel") Channel ch,
			@PathVariable("id") String id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (!StringUtils.hasText(id) || ch != null && StringUtils.hasText(ch.getTemplate())) {
			pageService.previewChannel(ch, req, resp);
		} else {
			pageService.previewChannelWithId(id, req, resp);
		}
	}
	
	@RequestMapping("/channel/{channelid}")
	public void channel(@PathVariable("channelid")String id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (logger.isTraceEnabled()) {
			traceRequest(req);
		}
		pageService.renderChannel(id, req, resp);
	}
	
	@RequestMapping("/ajax/{callid}")
	public void ajax(@PathVariable("callid")String id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (logger.isTraceEnabled()) {
			traceRequest(req);
		}
		pageService.ajaxCall(id, req, resp);
	}
	
	@RequestMapping("/fragment/{html}")
	public void htmlFragment(@PathVariable("html")String id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (logger.isTraceEnabled()) {
			traceRequest(req);
		}
		pageService.htmlFragment(id, req, resp);
	}

	@RequestMapping(value="/html/action/generate-channel", method=RequestMethod.POST)
	public @ResponseBody String generateChannel(@RequestParam("id") String id) {
		if (!StringUtils.hasText(id)) return "未指定要生成的页面。";
		try {
			pageService.generate(id);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/generate-all-channels")
	public @ResponseBody String generateAllChannels() {
		try {
			pageService.generateAll();
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/export-channels")
	public void exportAllChannels(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = channelService.exportAll();
		String downloadName = "channels.zip";
		if (logger.isDebugEnabled()) {
			logger.debug("Downloading exported channels as: " + fn);
		}
		downloadFile(resp, fn, downloadName);
	}
	
	@RequestMapping(value="/html/action/export-sections")
	public void exportAllSections(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = sectionService.exportAll();
		String downloadName = "sections.zip";
		if (logger.isDebugEnabled()) {
			logger.debug("Downloading exported sections as: " + fn);
		}
		downloadFile(resp, fn, downloadName);
	}
	
	@RequestMapping(value="/html/action/export-calls")
	public void exportAllCalls(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = callService.exportAll();
		String downloadName = "ajaxcalls.zip";
		if (logger.isDebugEnabled()) {
			logger.debug("Downloading exported calls as: " + fn);
		}
		downloadFile(resp, fn, downloadName);
	}
	
	@RequestMapping(value="/html/action/export-fragments")
	public void exportAllFragments(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = fragmentService.exportAll();
		String downloadName = "fragments.zip";
		if (logger.isDebugEnabled()) {
			logger.debug("Downloading exported fragments as: " + fn);
		}
		downloadFile(resp, fn, downloadName);
	}

	private void downloadFile(HttpServletResponse resp, String fn,
			String downloadName) throws FileNotFoundException, IOException {
		File f = new File(fn);
		resp.setContentType(JAR_CONTENT_TYPE);
		resp.setHeader("Content-Disposition", "attachment; filename=" + downloadName);
		resp.setContentLength((int) f.length());
		FileInputStream fin = new FileInputStream(f);
		OutputStream ros = resp.getOutputStream();
		try {
			int l = 0;
			byte[] buff = new byte[4096];
			while ((l = fin.read(buff)) > 0) {
				ros.write(buff, 0, l);
			}
		} finally {
			fin.close();
			f.delete();
		}
	}
	
	@RequestMapping(value="/html/action/import-channels", method=RequestMethod.POST)
	public @ResponseBody String importChannels(@RequestParam("importChannelsFile") MultipartFile file)
			throws IOException {
		InputStream ins = file.getInputStream();
		try {
			channelService.importAll(FileUtils.toTempFile(ins));
		} finally {
			ins.close();
		}
		return MSG_JSON_OK;
	}
	
	@RequestMapping(value="/html/action/import-sections", method=RequestMethod.POST)
	public @ResponseBody String importSections(@RequestParam("importSectionsFile") MultipartFile file)
			throws IOException {
		InputStream ins = file.getInputStream();
		try {
			sectionService.importAll(FileUtils.toTempFile(ins));
		} finally {
			ins.close();
		}
		return MSG_JSON_OK;
	}
	
	@RequestMapping(value="/html/action/import-calls", method=RequestMethod.POST)
	public @ResponseBody String importCalls(@RequestParam("importFile") MultipartFile file)
			throws IOException {
		InputStream ins = file.getInputStream();
		try {
			callService.importAll(FileUtils.toTempFile(ins));
		} finally {
			ins.close();
		}
		return MSG_JSON_OK;
	}
	
	@RequestMapping(value="/html/action/import-fragments", method=RequestMethod.POST)
	public @ResponseBody String importFragments(@RequestParam("importFile") MultipartFile file)
			throws IOException {
		InputStream ins = file.getInputStream();
		try {
			fragmentService.importAll(FileUtils.toTempFile(ins));
		} finally {
			ins.close();
		}
		return MSG_JSON_OK;
	}

	@RequestMapping(value="/html/action/save-channel", method=RequestMethod.POST)
	public @ResponseBody String saveChannel(@ModelAttribute("channel") Channel ch) {
		try {
			logger.trace("Channel saving request received: {}", ch);
			channelService.save(ch);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}

	private String translateExceptionMessage(Exception ex) {
		logger.error("", ex);
		String msg = ex.getMessage();
		if (!StringUtils.hasText(msg)) msg = "发生未知异常。";
		else msg = "发生异常，信息：" + msg;
		return msg;
	}
	
	@RequestMapping(value="/html/action/channel-template-save", method=RequestMethod.POST)
	public @ResponseBody String channelTemplateSave(@ModelAttribute("channel") Channel ch) {
		try {
			channelService.saveTemplate(ch);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-channel", method=RequestMethod.POST)
	public @ResponseBody String removeChannel(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				channelService.remove(id);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/save-section", method=RequestMethod.POST)
	public @ResponseBody String saveSection(@ModelAttribute("section") Section sec) {
		try {
			sectionService.save(sec);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-section", method=RequestMethod.POST)
	public @ResponseBody String removeSection(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				sectionService.remove(id);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/save-call", method=RequestMethod.POST)
	public @ResponseBody String saveCall(@ModelAttribute("call") AjaxCall call) {
		try {
			callService.save(call);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-call", method=RequestMethod.POST)
	public @ResponseBody String removeCall(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				callService.remove(id);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/save-fragment", method=RequestMethod.POST)
	public @ResponseBody String saveFragment(@ModelAttribute("call") Fragment fragment) {
		try {
			fragmentService.save(fragment);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	@RequestMapping(value="/html/action/del-fragment", method=RequestMethod.POST)
	public @ResponseBody String removeFragment(@RequestParam("id") String id) {
		try {
			if (StringUtils.hasText(id))
				fragmentService.remove(id);
			return MSG_ACTION_OK;
		} catch (Exception ex) {
			return translateExceptionMessage(ex);
		}
	}
	
	private String joinString(String[] strs) {
		StringBuilder buff = new StringBuilder();
		for (String s: strs) {
			if (buff.length() > 0) buff.append(',');
			buff.append(s);
		}
		return buff.toString();
	}
	
	private String joinString(Enumeration<String> strs) {
		StringBuilder buff = new StringBuilder();
		while (strs.hasMoreElements()) {
			if (buff.length() > 0) buff.append(',');
			String s = strs.nextElement();
			buff.append(s);
		}
		return buff.toString();
	}
	
	private void traceRequest(HttpServletRequest req) {
		logger.trace("-------------request parameters---------------");
		for (Entry<String, String[]> e: req.getParameterMap().entrySet()) {
			logger.trace("" + e.getKey() + "=" + joinString(e.getValue()));
		}
		logger.trace("-------------request headers------------");
		Enumeration<String> enums = req.getHeaderNames();
		while (enums.hasMoreElements()) {
			String k = enums.nextElement();
			logger.trace("" + k + "=" +  joinString(req.getHeaders(k)));
		}
	}
}

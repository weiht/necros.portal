/**
 * 
 */
package org.necros.portal.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.ConversionTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.necros.portal.ajax.AjaxCall;
import org.necros.portal.ajax.AjaxCallService;
import org.necros.portal.channel.Channel;
import org.necros.portal.channel.ChannelService;
import org.necros.portal.fragment.Fragment;
import org.necros.portal.fragment.FragmentService;
import org.necros.portal.section.Section;
import org.necros.portal.section.SectionService;
import org.necros.portal.util.FileUtils;
import org.necros.portal.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * @author weiht
 *
 */
public class VelocityPageService implements PageService, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(VelocityPageService.class);

	private static final String DEF_FILE_PREFIX = "file:///";
	
	private ChannelService channelService;
	private SectionService sectionService;
	private AjaxCallService callService;
	private FragmentService fragmentService;

	private VelocityPageConfig config;
	private Map<String, Object> serviceBeans;
	private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	@Override
	public void renderPage(String page,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		logger.debug("html:" + page);
		resp.setContentType("text/html; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		vctx.put("page", page);
		logger.debug("{}", vctx);
		evaluateResource(w, vctx, config.getTemplatePath() + config.getHeaderFile());
		if (StringUtils.hasText(config.getManagerHeaderFile())) {
			evaluateResource(w, vctx, config.getTemplatePath() + config.getManagerHeaderFile());
		}
		evaluateResource(w, vctx, config.getTemplatePath() + page + config.getTemplateExtension(), true);
		if (StringUtils.hasText(config.getManagerFooterFile())) {
			evaluateResource(w, vctx, config.getTemplatePath() + config.getManagerFooterFile());
		}
		evaluateResource(w, vctx, config.getTemplatePath() + config.getFooterFile());
	}

	@Override
	public void previewChannel(Channel ch, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		if (logger.isTraceEnabled()) {
			logger.trace("Preview channel: ");
			logger.trace("content: " + req.getContentType());
			logger.trace("id: " + ch.getId());
			logger.trace("template:\n" + ch.getTemplate());
		}
		resp.setContentType("text/html; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		vctx.put("channel", ch.getId());
		vctx.put("channelTitle", ch.getDisplayName());
		vctx.put("sectionEvaluator", new SectionEvaluator(w, this, vctx));
		logger.debug("{}", vctx);
		evaluateResource(w, vctx, config.getTemplatePath() + config.getHeaderFile());
		doEvaluateChannel(w, vctx, ch);
		evaluateResource(w, vctx, config.getTemplatePath() + config.getFooterFile());
	}
	
	@Override
	public void previewChannelWithId(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		logger.debug("channel:{}", id);
		resp.setContentType("text/html; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		vctx.put("channel", id);
		vctx.put("sectionEvaluator", new SectionEvaluator(w, this, vctx));
		logger.debug("{}", vctx);
		evaluateResource(w, vctx, config.getTemplatePath() + config.getHeaderFile());
		evaluateChannel(w, vctx, id);
		evaluateResource(w, vctx, config.getTemplatePath() + config.getFooterFile());
	}

	@Override
	public void renderChannel(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String fn = findChannelFile(id);
		File f = null;
		if (!StringUtils.hasText(fn) || !(f = new File(fn)).exists() || !f.isFile()) {
			previewChannelWithId(id, req, resp);
		} else {
			doRenderChannel(id, fn, req, resp);
		}
	}
	
	@Override
	public void ajaxCall(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		logger.debug("ajax call: " + id);
		AjaxCall call = callService.findById(id);
		if (call == null) {
			logger.warn("Ajax call not found.");
			resp.sendError(404);
		} else {
			doAjaxCall(call, req, resp);
		}
	}

	@Override
	public void htmlFragment(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		logger.debug("html fragment: " + id);
		Fragment frag = fragmentService.findById(id);
		if (frag == null) {
			logger.warn("HTML fragment not found.");
			resp.sendError(404);
		} else {
			doHtmlFragment(frag, req, resp);
		}
	}

	private void doHtmlFragment(Fragment frag, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		// TODO 根据返回类型来输出HTML和Javascript
		resp.setContentType("text/javascript; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		vctx.put("sectionEvaluator", new SectionEvaluator(w, this, vctx));
		if (logger.isTraceEnabled()) {
			logger.trace("{}", frag);
			logger.trace(frag.getTemplate());
		}
		BufferedReader r = evalFragment(frag, vctx);
		String ln;
		while ((ln = r.readLine()) != null) {
			w.write("document.write('");
			w.write(StringEscapeUtils.escapeJavaScript(ln));
			w.write("');");
		}
		w.flush();
	}

	private BufferedReader evalFragment(Fragment frag, VelocityContext vctx)
			throws IOException {
		StringWriter sw = new StringWriter();
		Velocity.evaluate(vctx, sw, "fragment~" + frag.getId(), frag.getTemplate());
		return new BufferedReader(new StringReader(sw.toString()));
	}

	private void doAjaxCall(AjaxCall call, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		//TODO 根据返回类型来输出不同的contentType
		resp.setContentType("text/html; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		if (logger.isTraceEnabled()) {
			logger.trace("{}", call);
			logger.trace(call.getTemplate());
		}
		Velocity.evaluate(vctx, w, "ajaxCall~" + call.getId(), call.getTemplate());
		w.flush();
	}

	private void doEvaluateResource(BufferedWriter w, VelocityContext vctx,
			String rn, boolean ret404) throws IOException {
		if (logger.isDebugEnabled())
			logger.debug("Evaluating resource: " + rn);
		Resource res = resolver.getResource(rn);
		if (res.exists()) {
			InputStream ins = res.getInputStream();
			try {
				Reader r = new InputStreamReader(ins);
				try {
					Velocity.evaluate(vctx, w, rn, r);
				} finally {
					r.close();
				}
			} finally {
				ins.close();
			}
			w.flush();
		} else {
			logger.warn("Resource not found: " + rn);
			if (ret404) {
				evaluateResource(w, vctx, config.getTemplatePath() + config.getNotfoundFile());
			}
		}
	}

	private void evaluateResource(BufferedWriter w, VelocityContext vctx,
			String rn) throws IOException {
		evaluateResource(w, vctx, rn, false);
	}

	private void evaluateResource(BufferedWriter w, VelocityContext vctx,
			String rn, boolean suppressError) throws IOException {
		try {
			doEvaluateResource(w, vctx, rn, suppressError);
		} catch (Throwable t) {
			if (suppressError) {
				logger.error("", t);
				evaluateResource(w, vctx, config.getTemplatePath() + config.getErrorFile());
			} else {
				throw new RuntimeException(t);
			}
		}
	}

	private void doEvaluateChannel(BufferedWriter w, VelocityContext vctx,
			Channel ch) throws IOException {
		evaluateResource(w, vctx, config.getTemplatePath() + config.getChannelHeaderFile(), false);
		if (logger.isTraceEnabled()) {
			logger.trace(ch.getTemplate());
		}
		Velocity.evaluate(vctx, w, "channel~" + ch.getId(), ch.getTemplate());
		evaluateResource(w, vctx, config.getTemplatePath() + config.getChannelFooterFile(), false);
	}
	
	private void evaluateChannel(BufferedWriter w, VelocityContext vctx,
			String id) throws IOException {
		Channel ch = channelService.findById(id);
		if (ch == null) {
			logger.warn("Channel not found: " + id);
			evaluateResource(w, vctx, config.getTemplatePath() + config.getNotfoundFile());
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace("Channl found: " + ch.getTemplate());
			}
			vctx.put("channelTitle", ch.getDisplayName());
			doEvaluateChannel(w, vctx, ch);
		}
	}
	
	private void initVelocityContext(VelocityContext vctx, HttpServletRequest req) {
		ensureServiceBeans();
		vctx.put("services", serviceBeans);
		vctx.put("converter", new ConversionTool());
		vctx.put("escapeTool", new EscapeTool());
		vctx.put("dateTool", new DateTool());
		vctx.put("stringEscapeUtils", new StringEscapeUtils());
		vctx.put("request", req);
		vctx.put("contextPath", RequestUtils.absoluteContextPath(req));
		vctx.put("extraHeader", config.getExtraHeader());
		vctx.put("extraFooter", config.getExtraFooter());
		if (logger.isDebugEnabled()) {
			logger.debug("extraHeader: " + config.getExtraHeader());
			logger.debug("extraFooter: " + config.getExtraFooter());
		}
	}

	private void ensureServiceBeans() {
		if (serviceBeans == null) {
			serviceBeans = new HashMap<String, Object>();
		}
		logger.trace("{}", serviceBeans);
	}

	@SuppressWarnings("unchecked")
	private void putBean(String beanName, Object bean) {
		if (beanName.indexOf('.') >= 0) {
			Map<String, Object> m = serviceBeans;
			String[] names = beanName.split("[\\.]");
			logger.debug("{}", names);
			for (int i = 0; i < names.length - 1; i ++) {
				String n = names[i];
				logger.debug(n);
				Object ib = m.get(n);
				if (ib == null) {
					ib = new HashMap<String, Object>();
					m.put(n, ib);
				}
				m = (Map<String, Object>)ib;
				logger.debug("{}", m);
			}
			m.put(names[names.length - 1], bean);
			logger.debug("{}", m);
		} else {
			serviceBeans.put(beanName, bean);
		}
	}
	
	void evaluateSection(BufferedWriter w, VelocityContext vctx,
			String id) throws IOException {
		Section sec = sectionService.findById(id);
		if (sec == null) {
			logger.warn("Section not found: " + id);
			evaluateResource(w, vctx, config.getTemplatePath() + config.getNotfoundFile());
		} else {
			if (logger.isTraceEnabled()) {
				logger.trace(sec.getTemplate());
			}
			try {
				Velocity.evaluate(vctx, w, "section~" + id, sec.getTemplate());
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				evaluateResource(w, vctx, config.getTemplatePath() + config.getErrorFile());
			}
		}
	}

	private String findChannelFile(String id) {
		String rootDir = config.getPageRootDir(),
				fileDir = config.getPageFileDir();
		String fn = FileUtils.combineFileName(rootDir, fileDir, id) + config.getTemplateExtension();
		if (StringUtils.hasText(fn)) {
			return fn;
		} else {
			return null;
		}
	}
	
	private String findChannelTempFile(String id) {
		String rootDir = config.getPageRootDir(),
				tempDir = config.getPageTempDir();
		String fn = FileUtils.combineFileName(rootDir, tempDir, id) + config.getTemplateExtension();
		if (StringUtils.hasText(fn)) {
			return fn;
		} else {
			return null;
		}
	}

	private void doRenderChannel(String id, String fn,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		logger.debug("channel file:" + fn);
		resp.setContentType("text/html; charset=utf-8");
		BufferedWriter w = new BufferedWriter(resp.getWriter());
		VelocityContext vctx = new VelocityContext();
		initVelocityContext(vctx, req);
		vctx.put("channel", id);
		Channel ch = getChannelService().findById(id);
		vctx.put("channelTitle", ch.getDisplayName());
		vctx.put("sectionEvaluator", new SectionEvaluator(w, this, vctx));
		logger.debug("{}", vctx);
		evaluateResource(w, vctx, DEF_FILE_PREFIX + fn);
	}
	
	@Override
	public void generate(String id) throws IOException {
		logger.debug("channel:" + id);
		Channel ch = channelService.findById(id);
		if (ch == null) throw new IOException("未找到指定的页面：" + id + "。");
		String fn = doGenerate(ch);
		moveTempToPage(fn, ch.getId());
	}

	private void moveTempToPage(String fn, String id) throws IOException {
		String pfn = findChannelFile(id);
		logger.debug("Page file: " + pfn);
		File pf = null;
		if (!StringUtils.hasText(pfn) ||
				(pf = new File(pfn)).exists() && !pf.isFile())
			throw new IOException("无法生成页面：" + pfn + "，请检查路径设置。");
		File p = pf.getParentFile();
		if (!p.exists()) p.mkdirs();
		if (pf.exists()) {
			pf.delete();
		}
		File f = new File(fn);
		f.renameTo(pf);
	}

	private String doGenerate(Channel ch) throws IOException {
		String fn = findChannelTempFile(ch.getId());
		logger.debug("channel file: " + fn);
		File f = null;
		if (!StringUtils.hasText(fn) ||
				(f = new File(fn)).exists() && !f.isFile())
			throw new IOException("无法生成页面：" + fn + "，请检查路径设置。");
		File p = f.getParentFile();
		if (!p.exists()) p.mkdirs();
		if (f.exists()) f.delete();
		f.createNewFile();
		BufferedWriter w = new BufferedWriter(new FileWriter(f));
		try {
			generateHeader(w);
			w.write(ch.getTemplate());
			generateFooter(w);
		} finally {
			w.close();
		}
		return fn;
	}

	private void generateHeader(BufferedWriter w) throws IOException {
		w.write(readFileStr(config.getTemplatePath() + config.getHeaderFile()));
		w.write(readFileStr(config.getTemplatePath() + config.getChannelHeaderFile()));
	}

	private void generateFooter(BufferedWriter w) throws IOException {
		w.write(readFileStr(config.getTemplatePath() + config.getChannelFooterFile()));
		w.write(readFileStr(config.getTemplatePath() + config.getFooterFile()));
	}

	private String readFileStr(String fn) throws IOException {
		logger.debug("reading file: " + fn);
		if (!StringUtils.hasText(fn)) return "";
		Resource res = resolver.getResource(fn);
		InputStream ins = res.getInputStream();
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(ins));
			StringBuilder b = new StringBuilder();
			String l = null;
			while ((l = r.readLine()) != null) {
				if (b.length() > 0) b.append("\n");
				b.append(l);
			}
			if (logger.isTraceEnabled()) {
				logger.trace(b.toString());
			}
			return b.toString();
		} finally {
			ins.close();
		}
	}

	@Override
	public void generateAll() throws IOException {
		List<Channel> channels = channelService.channelsOwnedBy(null);
		if (channels == null || channels.isEmpty()) {
			logger.warn("Failure generating pages. No channels found.");
			return;
		}
		for (Channel ch: channels) {
			doGenerate(ch);
		}
		for (Channel ch: channels) {
			String fn = findChannelTempFile(ch.getId());
			moveTempToPage(fn, ch.getId());
		}
	}

	public ChannelService getChannelService() {
		return channelService;
	}

	@Required
	public void setChannelService(ChannelService channelService) {
		this.channelService = channelService;
	}

	public SectionService getSectionService() {
		return sectionService;
	}

	@Required
	public void setSectionService(SectionService sectionService) {
		this.sectionService = sectionService;
	}

	public AjaxCallService getCallService() {
		return callService;
	}

	@Required
	public void setCallService(AjaxCallService callService) {
		this.callService = callService;
	}

	public VelocityPageConfig getConfig() {
		return config;
	}

	@Required
	public void setConfig(VelocityPageConfig config) {
		this.config = config;
	}

	@Override
	public void registerServiceBean(String name, Object bean) {
		ensureServiceBeans();
		putBean(name, bean);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String fn = "classpath:velocity.properties";
		Resource res = resolver.getResource(fn);
		if (res == null) {
			logger.warn("Velocity configuration file not found: " + fn);
		} else {
			Properties props = new Properties();
			InputStream ins = res.getInputStream();
			try {
				props.load(ins);
				if (logger.isTraceEnabled()) {
					logger.trace("{}", props);
				}
			} finally {
				ins.close();
			}
			Velocity.init(props);
		}
	}

	public FragmentService getFragmentService() {
		return fragmentService;
	}

	public void setFragmentService(FragmentService fragmentService) {
		this.fragmentService = fragmentService;
	}
}

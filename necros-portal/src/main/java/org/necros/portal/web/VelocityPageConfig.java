package org.necros.portal.web;

import org.springframework.util.StringUtils;

public class VelocityPageConfig {
	private String templatePath;
	private String templateExtension;
	private String headerFile;
	private String extraHeader;
	private String managerHeaderFile;
	private String footerFile;
	private String extraFooter;
	private String managerFooterFile;
	private String notfoundFile;
	private String errorFile;
	private String channelHeaderFile;
	private String channelFooterFile;
	private String pageRootDir;
	private String pageFileDir;
	private String pageTempDir;

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplateExtension() {
		return templateExtension;
	}

	public void setTemplateExtension(String templateExtension) {
		this.templateExtension = templateExtension;
	}

	public String getHeaderFile() {
		return headerFile;
	}

	public void setHeaderFile(String headerFile) {
		this.headerFile = headerFile;
	}

	public String getExtraHeader() {
		return extraHeader;
	}

	public void setExtraHeader(String extraHeader) {
		this.extraHeader = extraHeader;
	}

	public String getManagerHeaderFile() {
		return managerHeaderFile;
	}

	public void setManagerHeaderFile(String managerHeaderFile) {
		this.managerHeaderFile = managerHeaderFile;
	}

	public String getFooterFile() {
		return footerFile;
	}

	public void setFooterFile(String footerFile) {
		this.footerFile = footerFile;
	}

	public String getExtraFooter() {
		return extraFooter;
	}

	public void setExtraFooter(String extraFooter) {
		this.extraFooter = extraFooter;
	}

	public String getManagerFooterFile() {
		return managerFooterFile;
	}

	public void setManagerFooterFile(String managerFooterFile) {
		this.managerFooterFile = managerFooterFile;
	}

	public String getNotfoundFile() {
		return notfoundFile;
	}

	public void setNotfoundFile(String notfoundFile) {
		this.notfoundFile = notfoundFile;
	}

	public String getErrorFile() {
		return errorFile;
	}

	public void setErrorFile(String errorFile) {
		this.errorFile = errorFile;
	}

	public String getChannelHeaderFile() {
		return channelHeaderFile;
	}

	public void setChannelHeaderFile(String channelHeaderFile) {
		this.channelHeaderFile = channelHeaderFile;
	}

	public String getChannelFooterFile() {
		return channelFooterFile;
	}

	public void setChannelFooterFile(String channelFooterFile) {
		this.channelFooterFile = channelFooterFile;
	}

	public String getPageRootDir() {
		return StringUtils.hasText(pageRootDir) ? pageRootDir : System.getProperty("java.io.tmpdir");
	}

	public void setPageRootDir(String pageRootDir) {
		this.pageRootDir = pageRootDir;
	}

	public String getPageFileDir() {
		return pageFileDir;
	}

	public void setPageFileDir(String pageFileDir) {
		this.pageFileDir = pageFileDir;
	}

	public String getPageTempDir() {
		return pageTempDir;
	}

	public void setPageTempDir(String pageTempDir) {
		this.pageTempDir = pageTempDir;
	}
}

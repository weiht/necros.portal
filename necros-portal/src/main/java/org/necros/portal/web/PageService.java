package org.necros.portal.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.necros.portal.channel.Channel;

public interface PageService {
	public abstract void renderPage(String page, HttpServletRequest req,
			HttpServletResponse resp) throws IOException;

	public abstract void previewChannel(Channel ch, HttpServletRequest req,
			HttpServletResponse resp) throws IOException;

	public abstract void previewChannelWithId(String id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException;

	public abstract void renderChannel(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException;

	public abstract void ajaxCall(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException;

	public abstract void htmlFragment(String id, HttpServletRequest req,
			HttpServletResponse resp) throws IOException;

	public abstract void registerServiceBean(String name, Object bean);

	public abstract void generate(String id) throws IOException;

	public abstract void generateAll() throws IOException;
}
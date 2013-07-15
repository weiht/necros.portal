package org.necros.portal.web;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.velocity.VelocityContext;


public class SectionEvaluator {
	private BufferedWriter w;
	private VelocityPageService pageService;
	private VelocityContext vctx;
	
	public SectionEvaluator(BufferedWriter w, VelocityPageService pageSvc, VelocityContext vctx) {
		this.w = w;
		this.pageService = pageSvc;
		this.vctx = vctx;
	}
	
	public void evaluate(String id) throws IOException {
		pageService.evaluateSection(w, vctx, id);
	}
}

package net.simpleframework.module.cms.news.web;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.module.cms.Attachment;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.IAttachmentManager;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.attachments.AbstractAttachmentHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentAction extends AbstractAttachmentHandler {
	public static ICMSContext context = CMSContextFactory.get();

	protected News news(final PageRequestResponse rRequest) {
		News news = (News) rRequest.getRequestAttr("newsId");
		if (news == null) {
			news = context.getNewsManager().getBean(rRequest.getParameter("newsId"));
			if (news != null) {
				rRequest.setRequestAttr("newsId", news);
			}
		}
		return news;
	}

	@Override
	public ID getOwnerId(final ComponentParameter cParameter) {
		final News news = news(cParameter);
		return news != null ? news.getId() : null;
	}

	@Override
	public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
		final Map<String, Object> parameters = super.getFormParameters(cParameter);
		final News news = news(cParameter);
		if (news != null) {
			parameters.put("newsId", news.getId());
		}
		return parameters;
	}

	@Override
	public AttachmentFile getAttachmentById(final ComponentParameter cParameter, final String id)
			throws IOException {
		AttachmentFile attachmentFile = getUploadCache(cParameter).get(id);
		if (attachmentFile == null) {
			final IAttachmentManager attachmentMgr = context.getAttachmentManager();
			final Attachment attachment = attachmentMgr.getBean(id);
			attachmentFile = attachmentMgr.createAttachmentFile(attachment, getTempDir(cParameter));
		}
		return attachmentFile;
	}

	@Override
	public Map<String, AttachmentFile> attachments(final ComponentParameter cParameter)
			throws IOException {
		final Map<String, AttachmentFile> attachmentFiles = new LinkedHashMap<String, AttachmentFile>(
				super.attachments(cParameter));
		final ID newsId = getOwnerId(cParameter);
		if (newsId != null) {
			final IAttachmentManager attachmentMgr = context.getAttachmentManager();
			final IDataQuery<Attachment> dq = attachmentMgr.query(newsId);
			Attachment attachment;
			final String tempdir = getTempDir(cParameter);
			while ((attachment = dq.next()) != null) {
				final AttachmentFile attachmentFile = attachmentMgr.createAttachmentFile(attachment,
						tempdir);
				if (attachmentFile == null) {
					continue;
				}
				attachmentFiles.put(String.valueOf(attachment.getId()), attachmentFile);
			}
		}
		return attachmentFiles;
	}

	@Override
	public String getDownloadAction(final String id) {
		final StringBuilder sb = new StringBuilder();
		sb.append("$Actions['ViewTemplatePage_download']('id=").append(id).append("');");
		return sb.toString();
	}
}
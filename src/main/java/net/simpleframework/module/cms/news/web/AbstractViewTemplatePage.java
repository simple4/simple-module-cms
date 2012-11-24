package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.module.cms.Attachment;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.CMSException;
import net.simpleframework.module.cms.Comment;
import net.simpleframework.module.cms.IAttachmentManager;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.ICommentManager;
import net.simpleframework.module.cms.news.INewsCategoryManager;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.MVCContextFactory;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.PageRequestResponse;
import net.simpleframework.mvc.UrlForward;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.attachments.AttachmentUtils;
import net.simpleframework.mvc.component.ext.comments.AbstractCommentHandler;
import net.simpleframework.mvc.component.ext.comments.CommentBean;
import net.simpleframework.mvc.template.Err404Page;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.t2.RightPageletTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AbstractViewTemplatePage extends RightPageletTemplatePage {
	public static final ICMSContext context = CMSContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
		addImportCSS(getCssResourceHomePath(pParameter) + "/view.css");

		addAjaxRequest(pParameter, "ViewTemplatePage_download").setHandleMethod("doDownload");
	}

	public IForward doDownload(final ComponentParameter cParameter) {
		final Attachment attachment = context.getAttachmentManager().getBean(
				cParameter.getParameter("id"));
		final JavascriptForward js = new JavascriptForward();
		if (attachment != null) {
			final IAttachmentManager attachmentMgr = context.getAttachmentManager();
			try {
				final AttachmentFile af = attachmentMgr.createAttachmentFile(attachment,
						MVCContextFactory.config().getTmpdir().getAbsolutePath());
				js.append(AttachmentUtils.getDownloadLoc(af.getTopic(), af.getAttachment()
						.getAbsolutePath()));
			} catch (final IOException e) {
				throw CMSException.of(e);
			}
		} else {
			js.append("alert('").append($m("AbstractViewTemplatePage.0")).append("');");
		}
		return js;
	}

	@Override
	public IForward forward(final PageParameter pParameter) {
		final News news = context.getNewsManager().getBean(pParameter.getParameter("newsId"));
		if (news != null) {
			pParameter.setRequestAttr("$news", news);
			if (news.isAllowComments()) {
				addNewsCommentBean(pParameter);
			}
		} else {
			return new UrlForward(uriFor(Err404Page.class));
		}
		return super.forward(pParameter);
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		final NavigationButtons btns = super.getNavigationBar(pParameter);
		final News news = (News) pParameter.getRequestAttr("$news");
		final INewsCategoryManager newsCategoryMgr = context.getNewsCategoryManager();
		final ArrayList<NewsCategory> al = new ArrayList<NewsCategory>();
		NewsCategory category = newsCategoryMgr.getBean(news.getCategoryId());
		while (category != null) {
			al.add(category);
			category = newsCategoryMgr.getBean(category.getParentId());
		}
		for (int i = al.size() - 1; i >= 0; i--) {
			category = al.get(i);
			btns.add(new LinkElement(category.getText()).setHref(UrlCache.of(category)));
		}
		return btns;
	}

	@Override
	public KVMap createVariables(final PageParameter pParameter) {
		final KVMap kv = (KVMap) super.createVariables(pParameter);
		final News news = (News) pParameter.getRequestAttr("$news");
		kv.add("comment_id", "comment_" + news.getId());
		return kv;
	}

	protected void addNewsCommentBean(final PageParameter pParameter) {
		final News news = (News) pParameter.getRequestAttr("$news");
		addComponentBean(pParameter, CommentBean.class, NewsCommentHandler.class).setContainerId(
				"comment_" + news.getId());
	}

	public static class NewsCommentHandler extends AbstractCommentHandler {

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
		public IDataQuery<?> comments(final ComponentParameter cParameter) {
			return context.getCommentManager().query(getOwnerId(cParameter));
		}

		@Override
		public Object getCommentById(final ComponentParameter cParameter, final Object id) {
			return context.getCommentManager().getBean(id);
		}

		@Override
		public JavascriptForward deleteComment(final ComponentParameter cParameter, final Object id) {
			context.getCommentManager().delete(id);
			return super.deleteComment(cParameter, id);
		}

		@Override
		public JavascriptForward addComment(final ComponentParameter cParameter) {
			final ICommentManager commentMgr = context.getCommentManager();
			final Comment comment = commentMgr.createBean();
			final News news = news(cParameter);
			if (news != null) {
				comment.setContentId(news.getId());
			}
			comment.setCreateDate(new Date());
			comment.setUserId(MVCContextFactory.permission().getLoginId(cParameter));
			comment.setContent(cParameter.getParameter(PARAM_COMMENT));
			final Comment parent = commentMgr.getBean(cParameter.getParameter(PARAM_PARENTID));
			if (parent != null) {
				comment.setParentId(parent.getId());
			}
			commentMgr.insert(comment);
			return super.addComment(cParameter);
		}
	}
}
package net.simpleframework.module.cms.impl;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ado.db.common.DbTable;
import net.simpleframework.ctx.Module;
import net.simpleframework.ctx.ado.AbstractADOModuleContext;
import net.simpleframework.module.cms.Attachment;
import net.simpleframework.module.cms.AttachmentLob;
import net.simpleframework.module.cms.Comment;
import net.simpleframework.module.cms.IAttachmentManager;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.ICommentManager;
import net.simpleframework.module.cms.news.INewsCategoryManager;
import net.simpleframework.module.cms.news.INewsManager;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.module.cms.news.impl.NewsCategoryManager;
import net.simpleframework.module.cms.news.impl.NewsManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class CMSContext extends AbstractADOModuleContext implements ICMSContext {

	@Override
	public void onInit() throws Exception {
		super.onInit();

		dataServiceFactory.putEntityService(Attachment.class, new DbTable("sf_cms_attachment"))
				.putEntityService(AttachmentLob.class, new DbTable("sf_cms_attachment_lob", true))
				.putEntityService(Comment.class, new DbTable("sf_cms_comment"));
		dataServiceFactory.putEntityService(NewsCategory.class, new DbTable("sf_cms_news_category"))
				.putEntityService(News.class, new DbTable("sf_cms_news"));
	}

	@Override
	protected Module createModule() {
		return new Module().setName("simple-module-cms").setText($m("CMSContext.0")).setOrder(4);
	}

	@Override
	public IAttachmentManager getAttachmentManager() {
		return singleton(AttachmentManager.class);
	}

	@Override
	public ICommentManager getCommentManager() {
		return singleton(CommentManager.class);
	}

	@Override
	public INewsCategoryManager getNewsCategoryManager() {
		return singleton(NewsCategoryManager.class);
	}

	@Override
	public INewsManager getNewsManager() {
		return singleton(NewsManager.class);
	}
}

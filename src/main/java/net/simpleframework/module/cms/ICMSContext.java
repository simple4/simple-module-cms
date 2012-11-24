package net.simpleframework.module.cms;

import net.simpleframework.ctx.ado.IADOModuleContext;
import net.simpleframework.module.cms.news.INewsCategoryManager;
import net.simpleframework.module.cms.news.INewsManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ICMSContext extends IADOModuleContext {

	/**
	 * 获取附件管理器
	 * 
	 * @return
	 */
	IAttachmentManager getAttachmentManager();

	/**
	 * 获取评论管理器
	 * 
	 * @return
	 */
	ICommentManager getCommentManager();

	/**
	 * 获取新闻类目管理器
	 * 
	 * @return
	 */
	INewsCategoryManager getNewsCategoryManager();

	/**
	 * 获取新闻管理器
	 * 
	 * @return
	 */
	INewsManager getNewsManager();
}

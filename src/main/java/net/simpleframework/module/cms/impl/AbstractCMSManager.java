package net.simpleframework.module.cms.impl;

import java.io.Serializable;

import net.simpleframework.ctx.ado.AbstractBeanDbManager;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.impl.NewsCategoryManager;
import net.simpleframework.module.cms.news.impl.NewsManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractCMSManager<T extends Serializable> extends
		AbstractBeanDbManager<T, T> {
	@Override
	public ICMSContext getModuleContext() {
		return CMSContextFactory.get();
	}

	public NewsCategoryManager newsCategoryMgr() {
		return (NewsCategoryManager) getModuleContext().getNewsCategoryManager();
	}

	public NewsManager newsMgr() {
		return (NewsManager) getModuleContext().getNewsManager();
	}

	public AttachmentManager attachmentMgr() {
		return (AttachmentManager) getModuleContext().getAttachmentManager();
	}
}

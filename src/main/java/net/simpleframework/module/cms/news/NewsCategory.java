package net.simpleframework.module.cms.news;

import net.simpleframework.common.ID;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;
import net.simpleframework.module.cms.AbstractCategoryBean;
import net.simpleframework.module.cms.CMSContextFactory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsCategory extends AbstractCategoryBean {
	/* 查看模板 */
	private int viewTemplate;

	public int getViewTemplate() {
		return viewTemplate;
	}

	public void setViewTemplate(final int viewTemplate) {
		this.viewTemplate = viewTemplate;
	}

	@Override
	public void setParentId(final ID parentId) {
		((ITreeBeanManagerAware<NewsCategory>) CMSContextFactory.get().getNewsCategoryManager())
				.assertParentId(this, parentId);
		super.setParentId(parentId);
	}

	private static final long serialVersionUID = -1520445282796635254L;
}

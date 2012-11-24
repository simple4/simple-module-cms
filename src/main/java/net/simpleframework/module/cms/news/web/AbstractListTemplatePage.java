package net.simpleframework.module.cms.news.web;

import java.util.ArrayList;

import net.simpleframework.common.html.element.AbstractElement;
import net.simpleframework.common.html.element.LabelElement;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.INewsCategoryManager;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.t2.RightPageletTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class AbstractListTemplatePage extends RightPageletTemplatePage {
	public static ICMSContext context = CMSContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);
		addImportCSS(getCssResourceHomePath(pParameter) + "/list.css");
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		final NavigationButtons btns = super.getNavigationBar(pParameter);
		final INewsCategoryManager newsCategoryMgr = context.getNewsCategoryManager();
		NewsCategory category = newsCategoryMgr.getBean(pParameter.getParameter("categoryId"));
		final ArrayList<NewsCategory> al = new ArrayList<NewsCategory>();
		while (category != null) {
			al.add(category);
			category = newsCategoryMgr.getBean(category.getParentId());
		}
		for (int i = al.size() - 1; i >= 0; i--) {
			category = al.get(i);
			final String txt = category.getText();
			AbstractElement<?> link;
			if (i > 0) {
				link = new LinkElement(txt).setHref(UrlCache.of(category));
			} else {
				link = new LabelElement(txt);
			}
			btns.add(link);
		}
		return btns;
	}
}
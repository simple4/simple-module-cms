package net.simpleframework.module.cms.news.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.simpleframework.common.ClassUtils;
import net.simpleframework.common.ID;
import net.simpleframework.common.StringUtils;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.AbstractMVCPage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class UrlCache {

	static final Map<ID, String> listUrlCache = new ConcurrentHashMap<ID, String>(),
			viewUrlCache = new ConcurrentHashMap<ID, String>();

	@SuppressWarnings("unchecked")
	public static String of(final NewsCategory category) {
		final ID categoryId = category.getId();
		String url = listUrlCache.get(categoryId);
		if (!StringUtils.hasText(url)) {
			Class<? extends AbstractMVCPage> pageClass;
			try {
				pageClass = (Class<? extends AbstractMVCPage>) ClassUtils.forName(UrlCache.class
						.getPackage().getName() + ".ListTemplate" + category.getViewTemplate() + "Page");
			} catch (final ClassNotFoundException e) {
				pageClass = ListTemplate0Page.class;
			}
			listUrlCache.put(categoryId, url = AbstractMVCPage.uriFor(pageClass) + "?categoryId="
					+ categoryId);
		}
		return url;
	}

	@SuppressWarnings("unchecked")
	public static String of(final News news) {
		final ID newsId = news.getId();
		String url = viewUrlCache.get(newsId);
		if (!StringUtils.hasText(url)) {
			Class<? extends AbstractMVCPage> pageClass;
			try {
				final NewsCategory category = CMSContextFactory.get().getNewsCategoryManager()
						.getBean(news.getCategoryId());
				final int viewTemplate = category != null ? category.getViewTemplate() : 0;
				pageClass = (Class<? extends AbstractMVCPage>) ClassUtils.forName(UrlCache.class
						.getPackage().getName() + ".ViewTemplate" + viewTemplate + "Page");
			} catch (final ClassNotFoundException e) {
				pageClass = ViewTemplate0Page.class;
			}
			viewUrlCache.put(newsId, url = AbstractMVCPage.uriFor(pageClass) + "?newsId=" + newsId);
		}
		return url;
	}
}

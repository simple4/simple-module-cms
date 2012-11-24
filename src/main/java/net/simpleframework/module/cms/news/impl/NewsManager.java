package net.simpleframework.module.cms.news.impl;

import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.common.ado.IParamsValue;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.module.cms.impl.AbstractCMSManager;
import net.simpleframework.module.cms.impl.AttachmentManager;
import net.simpleframework.module.cms.news.INewsManager;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsManager extends AbstractCMSManager<News> implements INewsManager {

	@Override
	public IDataQuery<News> query(final NewsCategory category) {
		return category == null ? query("1=1 order by createdate desc") : query(
				"categoryId=? order by createdate desc", category.getId());
	}

	{
		addListener(new TableEntityAdapterEx() {

			@Override
			public void beforeDelete(final ITableEntityService service, final IParamsValue paramsValue) {
				super.beforeDelete(service, paramsValue);
				coll(paramsValue);
			}

			@Override
			public void afterDelete(final ITableEntityService service, final IParamsValue paramsValue) {
				super.afterDelete(service, paramsValue);
				final AttachmentManager attachmentMgr = attachmentMgr();
				for (final News news : coll(paramsValue)) {
					attachmentMgr.deleteWith("contentId=?", news.getId());
				}
			}
		});
	}
}

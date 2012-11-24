package net.simpleframework.module.cms.news;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.ctx.ado.IBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface INewsManager extends IBeanManagerAware<News> {

	/**
	 * 按类目查找
	 * 
	 * @param category
	 * @return
	 */
	IDataQuery<News> query(NewsCategory category);
}

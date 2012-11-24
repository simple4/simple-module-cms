package net.simpleframework.module.cms.impl;

import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.module.cms.Comment;
import net.simpleframework.module.cms.ICommentManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CommentManager extends AbstractCMSManager<Comment> implements ICommentManager {

	@Override
	public IDataQuery<Comment> query(final Object content) {
		if (content == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("contentId=? order by createDate desc",
				content instanceof IIdBeanAware ? ((IIdBeanAware) content).getId() : content);
	}

	@Override
	public IDataQuery<Comment> queryChildren(final Comment parent) {
		if (parent == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("parentid=? order by createDate desc", parent.getId());
	}
}

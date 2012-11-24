package net.simpleframework.module.cms;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.ctx.ado.IBeanManagerAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface ICommentManager extends IBeanManagerAware<Comment>, ITreeBeanManagerAware<Comment> {

	/**
	 * 获取文档的评论列表，parentId=null
	 * 
	 * @param content
	 * @return
	 */
	IDataQuery<Comment> query(Object content);
}

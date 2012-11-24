package net.simpleframework.module.cms;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractIdBean;
import net.simpleframework.common.bean.ITreeBeanAware;
import net.simpleframework.ctx.ado.ITreeBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Comment extends AbstractIdBean implements ITreeBeanAware {

	/* 拥有者id */
	private ID contentId;

	/* 父id */
	private ID parentId;

	/* 内容 */
	private String content;

	/* 创建日期 */
	private Date createDate;

	/* 创建人 */
	private ID userId;

	public ID getContentId() {
		return contentId;
	}

	public void setContentId(final ID contentId) {
		this.contentId = contentId;
	}

	@Override
	public ID getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(final ID parentId) {
		((ITreeBeanManagerAware<Comment>) CMSContextFactory.get().getCommentManager())
				.assertParentId(this, parentId);
		this.parentId = parentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	private static final long serialVersionUID = 7613770066758656538L;
}

package net.simpleframework.module.cms;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractIdBean;
import net.simpleframework.common.bean.IDescriptionBeanAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ContentLogBean extends AbstractIdBean implements IDescriptionBeanAware {

	/* 内容id */
	private ID contentId;

	/* 创建日期 */
	private Date createDate;

	/* 创建人 */
	private ID userId;

	/* 操作前、后状态 */
	private EContentStatus fromStatus, toStatus;

	/* 描述 */
	private String description;

	public ID getContentId() {
		return contentId;
	}

	public void setContentId(final ID contentId) {
		this.contentId = contentId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(final Date createDate) {
		this.createDate = createDate;
	}

	public ID getUserId() {
		return userId;
	}

	public void setUserId(final ID userId) {
		this.userId = userId;
	}

	public EContentStatus getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(final EContentStatus fromStatus) {
		this.fromStatus = fromStatus;
	}

	public EContentStatus getToStatus() {
		return toStatus;
	}

	public void setToStatus(final EContentStatus toStatus) {
		this.toStatus = toStatus;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	private static final long serialVersionUID = -3731218434155623793L;
}

package net.simpleframework.module.cms;

import java.util.Date;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractIdBean;
import net.simpleframework.common.bean.IDescriptionBeanAware;
import net.simpleframework.common.bean.IOrderBeanAware;
import net.simpleframework.common.html.HtmlUtils;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class AbstractContentBean extends AbstractIdBean implements IDescriptionBeanAware,
		IOrderBeanAware {
	/* 状态 */
	private EContentStatus status;

	/* 标题 */
	private String topic;

	/* 正文内容 */
	private String content;

	/* 创建日期 */
	private Date createDate;

	/* 创建人 */
	private ID userId;

	/* 描述 */
	private String description;

	/* 排序字段 */
	private long oorder;

	/* 统计信息-查看次数 */
	private long views;

	public EContentStatus getStatus() {
		return status == null ? EContentStatus.edit : status;
	}

	public void setStatus(final EContentStatus status) {
		this.status = status;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = HtmlUtils.stripScripts(content);
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

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public long getOorder() {
		return oorder;
	}

	@Override
	public void setOorder(final long oorder) {
		this.oorder = oorder;
	}

	public long getViews() {
		return views;
	}

	public void setViews(final long views) {
		this.views = views;
	}
}

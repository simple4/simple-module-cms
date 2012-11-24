package net.simpleframework.module.cms;

import net.simpleframework.common.ID;
import net.simpleframework.common.bean.AbstractIdBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class Attachment extends AbstractIdBean {
	/* 外键，文档id */
	private ID contentId;

	/* 摘要值，md5 */
	private String md5;

	/* 标题 */
	private String topic;

	/* 附件类型 */
	private String attachtype;

	/* 附件大小 */
	private long attachsize;

	/* 下载次数 */
	private int downloads;

	/* 描述 */
	private String description;

	public ID getContentId() {
		return contentId;
	}

	public void setContentId(final ID contentId) {
		this.contentId = contentId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(final String md5) {
		this.md5 = md5;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(final String topic) {
		this.topic = topic;
	}

	public String getAttachtype() {
		return attachtype;
	}

	public void setAttachtype(final String attachtype) {
		this.attachtype = attachtype;
	}

	public long getAttachsize() {
		return attachsize;
	}

	public void setAttachsize(final long attachsize) {
		this.attachsize = attachsize;
	}

	public int getDownloads() {
		return downloads;
	}

	public void setDownloads(final int downloads) {
		this.downloads = downloads;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	private static final long serialVersionUID = -2098251332418292650L;
}

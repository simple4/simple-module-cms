package net.simpleframework.module.cms;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentLob implements Serializable {

	/* 摘要值，唯一键值 */
	private String md;

	/* 附件 */
	private InputStream attachment;

	public String getMd() {
		return md;
	}

	public void setMd(final String md) {
		this.md = md;
	}

	public InputStream getAttachment() {
		return attachment;
	}

	public void setAttachment(final InputStream attachment) {
		this.attachment = attachment;
	}

	private static final long serialVersionUID = 1757957179977035488L;
}

package net.simpleframework.module.cms;

import java.io.IOException;
import java.util.Map;

import net.simpleframework.common.ID;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.ctx.ado.IBeanManagerAware;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IAttachmentManager extends IBeanManagerAware<Attachment> {

	/**
	 * 获取文档的附件列表
	 * 
	 * @param ContentBean
	 * @return
	 */
	IDataQuery<Attachment> query(Object content);

	/**
	 * 获取lob对象
	 * 
	 * @param attachment
	 * @return
	 */
	AttachmentLob getLob(Attachment attachment);

	/**
	 * 插入附件
	 * 
	 * @param contentId
	 * @param attachments
	 */
	void insert(ID contentId, Map<String, AttachmentFile> attachments);

	/**
	 * 
	 * @param attachment
	 * @param tmpdir
	 * @return
	 * @throws IOException
	 */
	AttachmentFile createAttachmentFile(Attachment attachment, String tmpdir) throws IOException;
}

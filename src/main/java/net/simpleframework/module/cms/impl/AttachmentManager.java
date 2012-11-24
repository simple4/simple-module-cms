package net.simpleframework.module.cms.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.simpleframework.ado.db.ITableEntityService;
import net.simpleframework.ado.db.common.ExpressionValue;
import net.simpleframework.common.ID;
import net.simpleframework.common.IoUtils;
import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.IParamsValue;
import net.simpleframework.common.ado.query.DataQueryUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.common.bean.IIdBeanAware;
import net.simpleframework.module.cms.Attachment;
import net.simpleframework.module.cms.AttachmentLob;
import net.simpleframework.module.cms.CMSException;
import net.simpleframework.module.cms.IAttachmentManager;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AttachmentManager extends AbstractCMSManager<Attachment> implements IAttachmentManager {

	@Override
	public IDataQuery<Attachment> query(final Object content) {
		if (content == null) {
			return DataQueryUtils.nullQuery();
		}
		return query("contentId=?",
				content instanceof IIdBeanAware ? ((IIdBeanAware) content).getId() : content);
	}

	@Override
	public AttachmentLob getLob(final Attachment attachment) {
		return getEntityService(AttachmentLob.class).queryBean(
				new ExpressionValue("md=?", attachment.getMd5()), AttachmentLob.class);
	}

	@Override
	public void insert(final ID contentId, final Map<String, AttachmentFile> attachments) {
		if (attachments == null || attachments.size() == 0) {
			return;
		}
		final ITableEntityService lobService = getEntityService(AttachmentLob.class);
		for (final Map.Entry<String, AttachmentFile> entry : attachments.entrySet()) {
			final AttachmentFile aFile = entry.getValue();
			final String md5 = entry.getKey();
			final Attachment attachment = createBean();
			attachment.setId(ID.Gen.id(aFile.getId()));
			attachment.setContentId(contentId);
			attachment.setTopic(aFile.getTopic());
			attachment.setMd5(md5);
			attachment.setAttachsize(aFile.getAttachment().length());
			final String ext = StringUtils.getFilenameExtension(aFile.getTopic());
			if (StringUtils.hasText(ext)) {
				attachment.setAttachtype(ext.toLowerCase());
			}
			insert(attachment);

			// lob
			if (lobService.count(new ExpressionValue("md=?", md5)) == 0) {
				final AttachmentLob lob = new AttachmentLob();
				lob.setMd(md5);
				try {
					lob.setAttachment(new FileInputStream(aFile.getAttachment()));
				} catch (final FileNotFoundException e) {
					throw CMSException.of(e);
				}
				lobService.insert(lob);
			}
		}
	}

	@Override
	public AttachmentFile createAttachmentFile(final Attachment attachment, final String tmpdir)
			throws IOException {
		if (attachment == null) {
			return null;
		}
		String filename = tmpdir + attachment.getMd5();
		final String ext = attachment.getAttachtype();
		if (StringUtils.hasText(ext)) {
			filename += "." + ext;
		}
		final File oFile = new File(filename);
		return new AttachmentFile(oFile, attachment.getMd5()) {
			@Override
			public File getAttachment() {
				if (!oFile.exists()) {
					final AttachmentLob lob = getModuleContext().getAttachmentManager().getLob(
							attachment);
					InputStream inputStream;
					if (lob == null || (inputStream = lob.getAttachment()) == null) {
						return null;
					}
					try {
						IoUtils.copyFile(inputStream, oFile);
					} catch (final IOException e) {
						log.error(e);
						return null;
					}
				}
				return super.getAttachment();
			};

			private static final long serialVersionUID = 3689368709808495226L;

		}.setSize(attachment.getAttachsize()).setTopic(attachment.getTopic())
				.setDownloads(attachment.getDownloads()).setDescription(attachment.getDescription());
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
				final ITableEntityService lobService = getEntityService(AttachmentLob.class);
				for (final Attachment attachment : coll(paramsValue)) {
					final String md5 = attachment.getMd5();
					if (count("md5=?", md5) == 0) {
						lobService.delete(new ExpressionValue("md=?", md5));
					}
				}
			}
		});
	}
}

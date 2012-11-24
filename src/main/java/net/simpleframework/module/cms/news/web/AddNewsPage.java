package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import net.simpleframework.common.Convert;
import net.simpleframework.common.bean.AttachmentFile;
import net.simpleframework.common.html.element.BlockElement;
import net.simpleframework.common.html.element.EInputType;
import net.simpleframework.common.html.element.InputElement;
import net.simpleframework.common.html.element.LabelElement;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.CMSException;
import net.simpleframework.module.cms.IAttachmentManager;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.INewsManager;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.AbstractMVCPage;
import net.simpleframework.mvc.IForwardCallback.IJavascriptForwardCallback;
import net.simpleframework.mvc.IPageHandler.PageSelector;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.base.validation.EValidatorMethod;
import net.simpleframework.mvc.component.base.validation.Validator;
import net.simpleframework.mvc.component.ext.attachments.AttachmentBean;
import net.simpleframework.mvc.component.ext.attachments.AttachmentUtils;
import net.simpleframework.mvc.component.ext.attachments.IAttachmentSaveCallback;
import net.simpleframework.mvc.component.ext.category.ICategoryHandler;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryBean;
import net.simpleframework.mvc.component.ui.dictionary.DictionaryTreeHandler;
import net.simpleframework.mvc.component.ui.htmleditor.EEditorToolbar;
import net.simpleframework.mvc.component.ui.htmleditor.HtmlEditorBean;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.FormTableRowTemplatePage;
import net.simpleframework.mvc.template.struct.RowField;
import net.simpleframework.mvc.template.struct.TableRow;
import net.simpleframework.mvc.template.struct.TextButtonInput;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class AddNewsPage extends FormTableRowTemplatePage {
	public static final ICMSContext context = CMSContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addComponentBean(pParameter, "AddNewsPage_editor", HtmlEditorBean.class)
				.setTextarea("ne_content").setToolbar(EEditorToolbar.News)
				.setJsLoadedCallback("$('idAddNewsPage_waiting').remove();").setHeight("240");
		addFormValidationBean(pParameter).addValidators(
				new Validator(EValidatorMethod.required, "#ne_topic, #ne_categoryText, #ne_content"));

		// 类目字典
		addComponentBean(pParameter, "AddNewsPage_dict_tree", TreeBean.class).setHandleClass(
				CategorySelectedTree.class);
		addComponentBean(pParameter, "AddNewsPage_dict", DictionaryBean.class)
				.setBindingId("ne_categoryId").setBindingText("ne_categoryText")
				.addTreeRef(pParameter, "AddNewsPage_dict_tree").setTitle($m("AddNewsPage.1"));

		// 上传
		addComponentBean(pParameter, "AddNewsPage_upload_page", AttachmentBean.class)
				.setInsertTextarea("ne_content").setHandleClass(AttachmentAction.class);
		addComponentBean(pParameter, "AddNewsPage_upload", WindowBean.class)
				.setContentRef("AddNewsPage_upload_page").setContentStyle("padding: 0;")
				.setResizable(false).setTitle($m("AddNewsPage.12")).setPopup(true).setHeight(480)
				.setWidth(340);
	}

	@Override
	public void onLoad(final PageParameter pParameter, final Map<String, Object> dataBinding,
			final PageSelector selector) {
		super.onLoad(pParameter, dataBinding, selector);
		NewsCategory category = null;
		final News news = context.getNewsManager().getBean(pParameter.getParameter("newsId"));
		if (news != null) {
			dataBinding.put("ne_id", news.getId());
			dataBinding.put("ne_topic", news.getTopic());
			dataBinding.put("ne_keyWords", news.getKeyWords());
			dataBinding.put("ne_source", news.getSource());
			dataBinding.put("ne_author", news.getAuthor());
			dataBinding.put("ne_content", news.getContent());
			dataBinding.put("ne_description", news.getDescription());
			dataBinding.put(className() + "_opt_allowComments", news.isAllowComments());
			category = context.getNewsCategoryManager().getBean(news.getCategoryId());
		}
		if (category == null) {
			category = context.getNewsCategoryManager().getBean(pParameter.getParameter("categoryId"));
		}
		if (category != null) {
			dataBinding.put("ne_categoryId", category.getId());
			dataBinding.put("ne_categoryText", category.getText());
		}
	}

	@Override
	public JavascriptForward doSave(final ComponentParameter cParameter) {
		final NewsCategory category = context.getNewsCategoryManager().getBean(
				cParameter.getParameter("ne_categoryId"));
		if (category == null) {
			throw CMSException.of($m("AddNewsPage.11"));
		}

		final INewsManager newsMgr = context.getNewsManager();
		News news = newsMgr.getBean(cParameter.getParameter("ne_id"));
		final boolean insert = news == null;
		if (insert) {
			news = newsMgr.createBean();
			news.setCreateDate(new Date());
			news.setUserId(permission().getLoginId(cParameter));
		}
		news.setCategoryId(category.getId());
		news.setTopic(cParameter.getParameter("ne_topic"));
		news.setKeyWords(cParameter.getParameter("ne_keyWords"));
		news.setSource(cParameter.getParameter("ne_source"));
		news.setAuthor(cParameter.getParameter("ne_author"));
		news.setContent(cParameter.getParameter("ne_content"));
		news.setDescription(cParameter.getParameter("ne_description"));
		news.setAllowComments(Convert.toBool(cParameter.getParameter(OPT_ALLOWCOMMENTS)));

		final News news2 = news;
		return doJavascriptForward(new IJavascriptForwardCallback() {
			@Override
			public void doAction(final JavascriptForward js) {
				final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
						"AddNewsPage_upload_page");
				AttachmentUtils.doSave(nComponentParameter, new IAttachmentSaveCallback() {
					@Override
					public void save(final Map<String, AttachmentFile> addQueue,
							final Set<String> deleteQueue) {
						final IAttachmentManager attachmentMgr = context.getAttachmentManager();
						if (insert) {
							newsMgr.insert(news2);
						} else {
							newsMgr.update(news2);
							if (deleteQueue != null) {
								attachmentMgr.delete(deleteQueue.toArray(new Object[] { deleteQueue.size() }));
							}
						}
						attachmentMgr.insert(news2.getId(), addQueue);
					}
				});
				js.append("$Actions['addNewsWindow'].close();");
				js.append("$Actions['").append(CategoryTableLCTemplatePage.COMPONENT_TABLE)
						.append("']();");
			}
		});
	}

	@Override
	public int labelWidth(final PageParameter pParameter) {
		return 70;
	}

	private final RowField r11 = new RowField($m("AddNewsPage.0"), new InputElement("ne_id",
			EInputType.hidden), new InputElement("ne_topic"));
	private final RowField r12 = new RowField($m("AddNewsPage.1"), new TextButtonInput(
			"ne_categoryText").setHiddenField("ne_categoryId").setOnclick(
			"$Actions['AddNewsPage_dict']()")).setElementsStyle("width:150px;");

	private final RowField r21 = new RowField($m("AddNewsPage.2"), new InputElement("ne_keyWords"));
	private final RowField r22 = new RowField($m("AddNewsPage.3"), new InputElement("ne_source"))
			.setElementsStyle("width:150px;");
	private final RowField r23 = new RowField($m("AddNewsPage.4"), new InputElement("ne_author"))
			.setElementsStyle("width:150px;");

	private final BlockElement waiting = new BlockElement("idAddNewsPage_waiting").setText(
			$m("AddNewsPage.9")).setStyle("float: left");
	private final RowField r41 = new RowField($m("AddNewsPage.6"), new InputElement(
			"ne_description", EInputType.textarea).setRows(2));

	@Override
	public String toTableRowsString(final PageParameter pParameter) {
		final News news = context.getNewsManager().getBean(pParameter.getParameter("newsId"));

		final LinkElement attach = new LinkElement($m("AddNewsPage.7"));
		if (news != null) {
			attach.setOnclick("$Actions['AddNewsPage_upload']('newsId=" + news.getId() + "');");
		} else {
			attach.setOnclick("$Actions['AddNewsPage_upload']();");
		}

		final RowField r31 = new RowField($m("AddNewsPage.5"), new BlockElement().setStyle(
				"text-align: right;padding: 0 4px 2px 0px;").addElements(waiting, attach),
				new InputElement("ne_content", EInputType.textarea));

		final StringBuilder sb = new StringBuilder();
		sb.append(new TableRow(r11, r12)).append(new TableRow(r21, r22, r23))
				.append(new TableRow(r31)).append(new TableRow(r41));
		sb.append("<style type=\"text/css\">");
		sb.append("span.cke_skin_kama {");
		sb.append("  border:  0 !important;");
		sb.append("  padding: 0 !important;");
		sb.append("}");
		sb.append("</style>");
		return sb.toString();
	}

	protected static final String OPT_ALLOWCOMMENTS = "opt_allowComments";

	@Override
	public String toOptString(final PageParameter pParameter) {
		final StringBuilder sb = new StringBuilder();
		final String id = className() + "_opt_allowComments";
		sb.append(new BlockElement().addElements(new InputElement(id, EInputType.checkbox)
				.setName(OPT_ALLOWCOMMENTS), new LabelElement($m("AddNewsPage.10")).setForId(id)));
		return sb.toString();
	}

	public static class CategorySelectedTree extends DictionaryTreeHandler {

		@Override
		public TreeNodes getTreenodes(final ComponentParameter cParameter, final TreeNode parent) {
			final ComponentParameter nComponentParameter = ComponentParameter.get(cParameter,
					AbstractMVCPage.get(NewsMgrPage.class).getCategoryBean());
			final ICategoryHandler cHandle = (ICategoryHandler) nComponentParameter
					.getComponentHandler();
			return cHandle.getCategoryDictTreenodes(nComponentParameter,
					(TreeBean) cParameter.componentBean, parent);
		}
	}
}

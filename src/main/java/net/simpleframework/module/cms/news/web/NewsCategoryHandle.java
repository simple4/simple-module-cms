package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.INewsCategoryManager;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ext.category.ctx.CategoryBeanAwareHandler;
import net.simpleframework.mvc.component.ui.tree.TreeBean;
import net.simpleframework.mvc.component.ui.tree.TreeNode;
import net.simpleframework.mvc.component.ui.tree.TreeNodes;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsCategoryHandle extends CategoryBeanAwareHandler<NewsCategory> {
	public static ICMSContext context = CMSContextFactory.get();

	@Override
	protected INewsCategoryManager beanMgr() {
		return context.getNewsCategoryManager();
	}

	@Override
	protected IDataQuery<?> categoryBeans(final ComponentParameter cParameter,
			final Object categoryId) {
		final INewsCategoryManager categoryMgr = beanMgr();
		return categoryMgr.queryChildren(categoryMgr.getBean(categoryId));
	}

	@Override
	public TreeNodes getCategoryTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode parent) {
		if (parent == null) {
			final TreeNodes nodes = TreeNodes.of();
			String text = $m("NewsCategoryHandle.0");
			text += "<br /><a class=\"addbtn a2\" onclick=\"$category_action(this).add();Event.stop(event);\">"
					+ $m("Add") + "</a>";
			final TreeNode tn = new TreeNode(treeBean, parent, text);
			tn.setOpened(true);
			tn.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
					+ "']('categoryId=');");
			nodes.add(tn);
			return nodes;
		} else {
			final Object o = parent.getDataObject();
			if (o instanceof NewsCategory) {
				final NewsCategory category = (NewsCategory) o;
				parent.setJsClickCallback("$Actions['" + CategoryTableLCTemplatePage.COMPONENT_TABLE
						+ "']('categoryId=" + category.getId() + "');");
			}
			return super.getCategoryTreenodes(cParameter, treeBean, parent);
		}
	}

	@Override
	public TreeNodes getCategoryDictTreenodes(final ComponentParameter cParameter,
			final TreeBean treeBean, final TreeNode treeNode) {
		return super.getCategoryTreenodes(cParameter, treeBean, treeNode);
	}

	@Override
	protected void onSave_setProperties(final ComponentParameter cParameter,
			final NewsCategory category, final boolean insert) {
		if (insert) {
			category.setCreateDate(new Date());
			category.setUserId(permission().getLoginId(cParameter));
		}
	}
}

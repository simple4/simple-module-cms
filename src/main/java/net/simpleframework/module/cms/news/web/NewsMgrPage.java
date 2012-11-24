package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.common.html.element.ElementList;
import net.simpleframework.common.html.element.LabelElement;
import net.simpleframework.common.html.element.LinkElement;
import net.simpleframework.ctx.permission.IPermissionHandler;
import net.simpleframework.module.cms.CMSContextFactory;
import net.simpleframework.module.cms.ICMSContext;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.module.cms.news.NewsCategory;
import net.simpleframework.mvc.IForward;
import net.simpleframework.mvc.JavascriptForward;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.menu.MenuBean;
import net.simpleframework.mvc.component.ui.menu.MenuItem;
import net.simpleframework.mvc.component.ui.menu.MenuItems;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.window.WindowBean;
import net.simpleframework.mvc.template.AbstractTemplatePage;
import net.simpleframework.mvc.template.Err404Page;
import net.simpleframework.mvc.template.struct.LinkButton;
import net.simpleframework.mvc.template.struct.LinkButtons;
import net.simpleframework.mvc.template.struct.NavigationButtons;
import net.simpleframework.mvc.template.struct.SearchInput;
import net.simpleframework.mvc.template.struct.TabButton;
import net.simpleframework.mvc.template.struct.TabButtons;
import net.simpleframework.mvc.template.t1.ext.CategoryTableLCTemplatePage;
import net.simpleframework.mvc.template.t1.ext.CategoryTablePagerHandler;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class NewsMgrPage extends CategoryTableLCTemplatePage {
	public static final ICMSContext context = CMSContextFactory.get();

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		addCategoryBean(pParameter, NewsCategoryHandle.class);

		addTablePagerBean(pParameter, NewsTableHandler.class)
				.addColumn(
						new TablePagerColumn("topic", $m("NewsMgrPage.1")).setTextAlign(ETextAlign.left))
				.addColumn(new TablePagerColumn("recommendation", $m("NewsMgrPage.2")).setWidth(80))
				.addColumn(new TablePagerColumn("comments", $m("NewsMgrPage.3")).setWidth(80))
				.addColumn(new TablePagerColumn("createDate", $m("NewsMgrPage.4")).setWidth(120))
				.addColumn(new TablePagerColumn("status", $m("NewsMgrPage.5")).setWidth(80))
				.addColumn(TablePagerColumn.ACTION);

		// add window
		addAjaxRequest(pParameter, "addNewsPage", AddNewsPage.class);
		addComponentBean(pParameter, "addNewsWindow", WindowBean.class).setContentRef("addNewsPage")
				.setTitle($m("NewsMgrPage.6")).setWidth(880).setHeight(630);

		// delete
		addAjaxRequest(pParameter, "ajaxNewsDelete").setConfirmMessage($m("Confirm.Delete"))
				.setHandleMethod("doNewsDelete");
	}

	@Override
	public String getRole(final PageParameter pParameter) {
		return IPermissionHandler.sj_all_account;
	}

	public IForward doNewsDelete(final ComponentParameter cParameter) {
		final Object[] ids = StringUtils.split(cParameter.getParameter("id"));
		if (ids != null) {
			context.getNewsManager().delete(ids);
		}
		return new JavascriptForward("$Actions['").append(COMPONENT_TABLE).append("']();");
	}

	@Override
	protected LinkButtons getToolbarButtons(final PageParameter pParameter) {
		final LinkButton add = new LinkButton($m("NewsMgrPage.7"));
		final LinkButtons btns = LinkButtons.of(add);
		final NewsCategory category = (NewsCategory) pParameter.getRequestAttr("select_category");
		if (category != null) {
			add.setOnclick("$Actions['addNewsWindow']('categoryId=" + category.getId() + "');");
			btns.append(
					LinkButton.SEP,
					new LinkButton($m("NewsMgrPage.8")).setOnclick("$Actions.loc('"
							+ UrlCache.of(category) + "', true);"));
		} else {
			add.setOnclick("$Actions['addNewsWindow']();");
		}
		btns.append(LinkButton.SEP, act_btn("ajaxNewsDelete", $m("Delete")));
		return btns;
	}

	@Override
	protected SearchInput getSearchInput(final PageParameter pParameter) {
		return new SearchInput().setOnSearchClick("alert('ok');").setInputWidth(200);
	}

	@Override
	public NavigationButtons getNavigationBar(final PageParameter pParameter) {
		return super.getNavigationBar(pParameter).append(new LabelElement($m("NewsMgrPage.0")));
	}

	@Override
	protected TabButtons getTabButtons(final PageParameter pParameter) {
		return TabButtons.of(new TabButton($m("NewsMgrPage.0"), uriFor(NewsMgrPage.class)));
	}

	public static class NewsTableHandler extends CategoryTablePagerHandler {

		@Override
		public Map<String, Object> getFormParameters(final ComponentParameter cParameter) {
			return ((KVMap) super.getFormParameters(cParameter)).add("categoryId",
					cParameter.getParameter("categoryId"));
		}

		@Override
		protected ElementList navigationTitle(final ComponentParameter cParameter) {
			return doNavigationTitle(cParameter,
					(NewsCategory) cParameter.getRequestAttr("select_category"),
					new NavigationTitleCallback<NewsCategory>() {
						@Override
						protected String rootText() {
							return $m("NewsCategoryHandle.0");
						}

						@Override
						protected NewsCategory get(final Object id) {
							return context.getNewsCategoryManager().getBean(id);
						}
					});
		}

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			final NewsCategory category = context.getNewsCategoryManager().getBean(
					cParameter.getParameter("categoryId"));
			if (category != null) {
				cParameter.setRequestAttr("select_category", category);
			}
			return context.getNewsManager().query(category);
		}

		@Override
		public MenuItems getContextMenu(final ComponentParameter cParameter, final MenuBean menuBean,
				final MenuItem menuItem) {
			if (menuItem == null) {
				return MenuItems
						.of(MenuItem.of(menuBean, $m("NewsMgrPage.8"), null, "$Actions.loc('"
								+ uriFor(ViewControlPage.class)
								+ "?newsId=' + $pager_action(item).rowId(), true);"))
						.append(MenuItem.sep(menuBean))
						.append(
								MenuItem
										.of(menuBean, $m("Edit"), MenuItem.ICON_EDIT,
												"$Actions['addNewsWindow']('newsId=' + $pager_action(item).rowId());"))
						.append(
								MenuItem.of(menuBean, $m("Delete"), MenuItem.ICON_DELETE,
										"$Actions['ajaxNewsDelete']('id=' + $pager_action(item).rowId());"));
			}
			return null;
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final News news = (News) dataObject;
					final Map<String, Object> kv = super.getRowData(cParameter, dataObject);
					kv.put("topic", new LinkElement(news.getTopic())
							.setOnclick("$Actions['addNewsWindow']('newsId=" + news.getId() + "');"));
					kv.put(TablePagerColumn.ACTION.getColumnName(), ACTIONc);
					return kv;
				}
			};
		}
	}

	public static class ViewControlPage extends AbstractTemplatePage {
		@Override
		protected String getRedirectUrl(final PageParameter pParameter) {
			final News news = CMSContextFactory.get().getNewsManager()
					.getBean(pParameter.getParameter("newsId"));
			return news == null ? uriFor(Err404Page.class) : UrlCache.of(news);
		}
	}
}

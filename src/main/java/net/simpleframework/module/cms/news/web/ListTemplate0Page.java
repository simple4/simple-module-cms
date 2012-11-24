package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.ado.query.IDataQuery;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.HtmlUtils;
import net.simpleframework.common.html.element.ETextAlign;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.lib.org.jsoup.nodes.Document;
import net.simpleframework.lib.org.jsoup.nodes.Element;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.mvc.PageParameter;
import net.simpleframework.mvc.common.ImageCache;
import net.simpleframework.mvc.component.ComponentParameter;
import net.simpleframework.mvc.component.ui.pager.AbstractTablePagerSchema;
import net.simpleframework.mvc.component.ui.pager.EPagerBarLayout;
import net.simpleframework.mvc.component.ui.pager.TablePagerBean;
import net.simpleframework.mvc.component.ui.pager.TablePagerColumn;
import net.simpleframework.mvc.component.ui.pager.db.AbstractDbTablePagerHandler;
import net.simpleframework.mvc.template.t2.struct.Pagelet;
import net.simpleframework.mvc.template.t2.struct.PageletTitle;
import net.simpleframework.mvc.template.t2.struct.Pagelets;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ListTemplate0Page extends AbstractListTemplatePage {

	@Override
	protected void onInit(final PageParameter pParameter) {
		super.onInit(pParameter);

		final TablePagerBean tablePager = (TablePagerBean) addComponentBean(pParameter,
				TablePagerBean.class, NewsList.class).setShowFilterBar(false).setShowLineNo(true)
				.setShowCheckbox(false).setExportAction("false")
				.setPagerBarLayout(EPagerBarLayout.bottom).setPageItems(30)
				.setContainerId("idNewsList");
		tablePager.addColumn(new TablePagerColumn("topic", $m("ListTemplate0Page.0"))
				.setTextAlign(ETextAlign.left).setSort(false).setNowrap(false));
	}

	@Override
	protected String toHtml(final PageParameter pParameter, final Map<String, Object> variables,
			final String currentVariable) throws IOException {
		return "<div id='idNewsList'></div>";
	}

	@Override
	protected Pagelets getPagelets() {
		return Pagelets.of(new Pagelet(new PageletTitle("推荐新闻"), "list"), new Pagelet(
				new PageletTitle("关注新闻"), "list"));
	}

	public static class NewsList extends AbstractDbTablePagerHandler {

		@Override
		public IDataQuery<?> createDataObjectQuery(final ComponentParameter cParameter) {
			return context.getNewsManager().query(
					context.getNewsCategoryManager().getBean(cParameter.getParameter("categoryId")));
		}

		@Override
		public AbstractTablePagerSchema createTablePagerSchema() {
			return new DefaultTablePagerSchema() {
				@Override
				public Map<String, Object> getRowData(final ComponentParameter cParameter,
						final Object dataObject) {
					final KVMap kv = new KVMap();
					final News news = (News) dataObject;
					final Document doc = HtmlUtils.createHtmlDocument(news.getContent(), false);
					final StringBuilder t = new StringBuilder();

					t.append("<a class='f3 nt' target='_blank' href=\"").append(UrlCache.of(news))
							.append("\">").append(news.getTopic()).append("</a>");
					final Date d = news.getCreateDate();
					t.append("<div class='time'>");
					t.append("<div class='month'>").append(String.format("%tb", d)).append("</div>");
					t.append("<div class='day'>").append(String.format("%te", d)).append("</div>");
					t.append("<div class='year'>").append(String.format("%tY", d)).append("</div>");
					t.append("</div>");
					t.append("<div class='nc'>");
					t.append("<img class=\"photo_icon\" src=\"");
					final Element img = doc.select("img").first();
					t.append(new ImageCache(img == null ? null : img.attr("src")).getPath(cParameter));
					t.append("\" />");
					String desc = news.getDescription();
					if (StringUtils.hasText(desc)) {
						desc = StringUtils.substring(desc, 300, true);
						desc = HtmlUtils.convertHtmlLines(desc);
					} else {
						desc = HtmlUtils.truncateHtml(doc, 300, true, true, true);
					}
					t.append(desc);
					t.append("<div class='nd'>");
					t.append(news.getComments()).append("&nbsp;").append($m("ListTemplate0Page.1"));
					t.append(SpanElement.SEP);
					t.append(news.getViews()).append("&nbsp;").append($m("ViewTemplate0Page.3"));
					t.append("</div>");
					t.append("</div>");
					kv.put("topic", t.toString());
					return kv;
				}
			};
		}
	}
}

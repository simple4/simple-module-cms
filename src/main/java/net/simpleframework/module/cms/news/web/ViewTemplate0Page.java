package net.simpleframework.module.cms.news.web;

import static net.simpleframework.common.I18n.$m;

import java.util.Date;

import net.simpleframework.common.StringUtils;
import net.simpleframework.common.coll.KVMap;
import net.simpleframework.common.html.element.SpanElement;
import net.simpleframework.module.cms.news.News;
import net.simpleframework.mvc.PageParameter;
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
public class ViewTemplate0Page extends AbstractViewTemplatePage {

	@Override
	public KVMap createVariables(final PageParameter pParameter) {
		final KVMap kv = super.createVariables(pParameter);
		final News news = (News) pParameter.getRequestAttr("$news");
		kv.add("news_topic", news.getTopic()).add("news_content", news.getContent());
		final Date createDate = news.getCreateDate();
		kv.add("news_cd_year", String.format("%tY", createDate))
				.add("news_cd_month", String.format("%tb", createDate))
				.add("news_cd_day", String.format("%te", createDate));
		return kv;
	}

	public String toTopic2HTML(final PageParameter pParameter) {
		final StringBuilder sb = new StringBuilder();
		final News news = (News) pParameter.getRequestAttr("$news");
		final String author = news.getAuthor();
		if (StringUtils.hasText(author)) {
			sb.append($m("ViewTemplate0Page.0")).append(": ");
			sb.append(author).append(SpanElement.SEP);
		}
		final String source = news.getSource();
		if (StringUtils.hasText(source)) {
			sb.append($m("ViewTemplate0Page.1")).append(": ");
			sb.append(source).append(SpanElement.SEP);
		}
		final String keyWords = news.getKeyWords();
		if (StringUtils.hasText(keyWords)) {
			sb.append($m("ViewTemplate0Page.2")).append(": ");
			sb.append(keyWords).append(SpanElement.SEP);
		}
		sb.append(news.getViews()).append("&nbsp;").append($m("ViewTemplate0Page.3"));
		return sb.toString();
	}

	@Override
	public String getTitle(final PageParameter pParameter) {
		final News news = (News) pParameter.getRequestAttr("$news");
		return news.getTopic();
	}

	@Override
	protected Pagelets getPagelets() {
		return Pagelets.of(new Pagelet(new PageletTitle("推荐新闻"), "test"), new Pagelet(
				new PageletTitle("关注新闻"), "test"));
	}
}

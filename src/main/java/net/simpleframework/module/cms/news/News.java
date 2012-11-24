package net.simpleframework.module.cms.news;

import net.simpleframework.common.ID;
import net.simpleframework.module.cms.AbstractContentBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class News extends AbstractContentBean {
	/* 类目id */
	private ID categoryId;

	/* 主题词 */
	private String keyWords;

	/* 作者 */
	private String author;

	/* 来源 */
	private String source;

	/* 推荐级别 >=0 */
	private int recommendation;

	/* 图片新闻 */
	private boolean imageMark;

	/* 统计信息-评论数和关注数。此信息需要和关联表同步 */
	private int comments, attentions;

	/* 是否允许评论 */
	private boolean allowComments = true;

	public ID getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(final ID categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(final String keyWords) {
		this.keyWords = keyWords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public int getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(final int recommendation) {
		this.recommendation = recommendation;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(final int comments) {
		this.comments = comments;
	}

	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(final boolean allowComments) {
		this.allowComments = allowComments;
	}

	public int getAttentions() {
		return attentions;
	}

	public void setAttentions(final int attentions) {
		this.attentions = attentions;
	}

	public boolean isImageMark() {
		return imageMark;
	}

	public void setImageMark(final boolean imageMark) {
		this.imageMark = imageMark;
	}

	private static final long serialVersionUID = 580033858128590717L;
}

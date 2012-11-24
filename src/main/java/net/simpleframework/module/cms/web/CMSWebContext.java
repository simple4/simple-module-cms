package net.simpleframework.module.cms.web;

import static net.simpleframework.common.I18n.$m;
import net.simpleframework.ctx.Module;
import net.simpleframework.module.cms.impl.CMSContext;
import net.simpleframework.module.cms.news.web.NewsMgrPage;
import net.simpleframework.mvc.ctx.WebModuleFunction;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CMSWebContext extends CMSContext {

	@Override
	protected Module createModule() {
		return super.createModule().setDefaultFunction(
				new WebModuleFunction(NewsMgrPage.class).setName("simple-module-cms-NewsMgrPage")
						.setText($m("CMSContext.0")));
	}
}

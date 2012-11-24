package net.simpleframework.module.cms;

import net.simpleframework.common.SimpleRuntimeException;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class CMSException extends SimpleRuntimeException {

	public CMSException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public static CMSException of(final String msg) {
		return _of(CMSException.class, msg, null);
	}

	public static CMSException of(final Throwable throwable) {
		return _of(CMSException.class, null, throwable);
	}

	private static final long serialVersionUID = -3871370008470558392L;
}

package net.simpleframework.module.cms;

import static net.simpleframework.common.I18n.$m;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public enum EContentStatus {
	edit {
		@Override
		public String toString() {
			return $m("EContentStatus.edit");
		}
	},

	audit {
		@Override
		public String toString() {
			return $m("EContentStatus.audit");
		}
	},

	publish {
		@Override
		public String toString() {
			return $m("EContentStatus.publish");
		}
	},

	lock {
		@Override
		public String toString() {
			return $m("EContentStatus.lock");
		}
	},

	abort {
		@Override
		public String toString() {
			return $m("EContentStatus.abort");
		}
	},

	delete {
		@Override
		public String toString() {
			return $m("EContentStatus.delete");
		}
	}
}

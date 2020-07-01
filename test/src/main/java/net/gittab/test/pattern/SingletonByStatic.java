package net.gittab.test.pattern;

/**
 * Singleton.
 *
 * @author xiaohua zhou
 **/
public final class SingletonByStatic {

	private SingletonByStatic() {
	}

	public SingletonByStatic getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {

		private static final SingletonByStatic INSTANCE = new SingletonByStatic();

	}

}

package net.gittab.githubtravis.pattern;

/**
 * SingletonByEnum.
 *
 * @author xiaohua zhou
 **/
public enum SingletonByEnum {

	/**
	 * SingletonByEnum instance.
	 */
	INSTANCE;

	private Singleton singleton = new Singleton();

	SingletonByEnum() {
		// Initialization configuration which involves
		// overriding defaults like delivery strategy
	}

	public Singleton getSingleton() {
		return this.singleton;
	}

	public static SingletonByEnum getInstance() {
		return INSTANCE;
	}

}

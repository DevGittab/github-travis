package net.gittab.githubtravis.pattern;

/**
 * ThreadSafeSingleton.
 *
 * @author xiaohua zhou
 **/
public class ThreadSafeSingleton {

	private volatile static ThreadSafeSingleton singleton;

	private ThreadSafeSingleton() {
	}

	public ThreadSafeSingleton getInstance() {
		// if(singleton == null){
		// synchronized(ThreadSafeSingleton.class){
		// if(singleton == null){
		// singleton = new ThreadSafeSingleton();
		// }
		// }
		// }
		// return singleton;
		if (singleton != null) {
			return singleton;
		}
		synchronized (ThreadSafeSingleton.class) {
			if (singleton == null) {
				singleton = new ThreadSafeSingleton();
			}
		}
		return singleton;
	}

}

package net.gittab.githubtravis.test.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * MultiThread.
 *
 * @author xiaohua zhou
 **/
public class MultiThread {

	public static void main(String[] args) {
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threadInfos = mxBean.dumpAllThreads(false, false);
		for (ThreadInfo threadInfo : threadInfos) {
			System.out.println("thread id is " + threadInfo.getThreadId()
					+ ", thread name is " + threadInfo.getThreadName());
		}
	}

}

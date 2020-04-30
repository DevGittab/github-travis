package net.gittab.githubtravis.configuration;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ExecutorConfig 线程池配置.
 *
 * @author xiaohua zhou
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 设置核心线程数
		executor.setCorePoolSize(5);
		// 设置最大线程数
		executor.setMaxPoolSize(1000);
		// 设置队列容量
		executor.setQueueCapacity(1000);
		// 设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(30000);
		// 设置默认线程名称
		executor.setThreadNamePrefix("user-copyright-tasks-");
		// 设置拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 等待所有任务结束后再关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(true);
		return executor;
	}

}

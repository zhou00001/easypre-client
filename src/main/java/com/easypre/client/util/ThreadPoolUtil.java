package com.easypre.client.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池工具类
 *
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class ThreadPoolUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolUtil.class);
	public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
	/**
	 * 线程池等待队列的长度
	 */
	private static final int DEFAULT_QUEUE_SIZE = 50;
	/**
	 * 创建线程池默认核心线程数
	 */
	private static final int DEFAULT_CORE_POOL_SIZE = AVAILABLE_PROCESSORS * 2;
	/**
	 * 创建线程池默认最大线程数
	 */
	private static final int DEFAULT_MAX_POOL_SIZE = AVAILABLE_PROCESSORS * 4;
	/**
	 * 全局线程池名字
	 */
	private static final String GLOBAL_POOL_NAME = "default";
	/**
	 * 全局线程池默认核心线程数
	 */
	private static final int GLOBAL_CORE_POOL_SIZE = 30;
	/**
	 * 全局线程池默认最大线程数
	 */
	private static final int GLOBAL_MAX_POOL_SIZE = 50;
	/**
	 * 线程池map
	 */
	private static ConcurrentHashMap<String, ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<>();

	/**
	 * 默认线程池统一执行，默认线程池大小：
	 *
	 * @param runnable
	 */
	public static void execute(Runnable runnable) {
		execute(GLOBAL_POOL_NAME, runnable, GLOBAL_CORE_POOL_SIZE, GLOBAL_MAX_POOL_SIZE);
	}

	/**
	 * 默认线程池统一执行，默认线程池大小：
	 *
	 * @param runnable
	 */
	public static void execute(String name, Runnable runnable) {
		execute(name, runnable, DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
	}

	/**
	 * 从线程池运行
	 *
	 * @param name
	 * @param runnable
	 * @param corePoolSize
	 * @param maxPoolSize
	 */
	public static void execute(String name, Runnable runnable, Integer corePoolSize, Integer maxPoolSize) {
		ThreadPoolExecutor poolExecutor = buildThreadPoolExecutor(name, corePoolSize, maxPoolSize);
		poolExecutor.execute(runnable);
	}

	/**
	 * 获取全局默认的线程池
	 *
	 * @return
	 */
	public static ThreadPoolExecutor getGlobalDefaultPoolExecutor() {
		return buildThreadPoolExecutor(GLOBAL_POOL_NAME, GLOBAL_CORE_POOL_SIZE, GLOBAL_MAX_POOL_SIZE);
	}

	/**
	 * 构建线程池
	 *
	 * @param name
	 * @param corePoolSize
	 * @param maxPoolSize
	 * @return
	 */
	public static ThreadPoolExecutor buildThreadPoolExecutor(String name, Integer corePoolSize, Integer maxPoolSize) {
		if (StringUtils.isBlank(name)) {
			name = GLOBAL_POOL_NAME;
		}
		if (!threadPoolMap.containsKey(name)) {
			synchronized (ThreadPoolUtil.class) {
				if (!threadPoolMap.containsKey(name)) {
					ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize
							, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE)
							, new ThreadPoolFactory(name)
							, new RejectedExecutionHandler() {
						@Override
						public void rejectedExecution(Runnable r,
													  ThreadPoolExecutor executor) {
							try {
								// 利用ABQ的阻塞put方法，阻塞调用线程，使得在工作队列
								// 满了之后不再继续提交任务（防止过度消耗资源导致宕机）
								executor.getQueue().put(r);
							} catch (InterruptedException e) {
								LOGGER.error("创建线程池异常", e);
							}
						}
					});
					threadPoolMap.put(name, threadPoolExecutor);
				}
			}
		}
		return threadPoolMap.get(name);
	}

	/**
	 * 获取线程工厂类
	 *
	 * @param name
	 * @return
	 */
	public static ThreadFactory getThreadFactory(String name) {
		Preconditions.checkArgument(StringUtils.isNotBlank(name));
		return new ThreadPoolFactory(name);
	}

	/**
	 * 线程池方法类
	 */
	static class ThreadPoolFactory implements ThreadFactory {
		private String name;
		private int counter;

		public ThreadPoolFactory(String name) {
			this.name = name;
			counter = 0;
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r, name + "-thread-" + counter);
			counter++;
			return t;
		}
	}
}

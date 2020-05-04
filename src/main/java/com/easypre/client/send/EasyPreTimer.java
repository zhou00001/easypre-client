package com.easypre.client.send;

import com.easypre.client.config.EasyPreConfig;
import com.easypre.client.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class EasyPreTimer {
	private static final Logger LOGGER= LoggerFactory.getLogger(EasyPreTimer.class);

	private static final String FACTORY_NAME="EasyPreSender";

	private static ScheduledExecutorService executorService;

	/**
	 * 初始化
	 */
	public static void init(EasyPreConfig config){
		LOGGER.info("EasyPre Timer init");
		int lingerMs=config.getLingerMs()!=null?config.getLingerMs():200;
		executorService=Executors.newSingleThreadScheduledExecutor(ThreadPoolUtil.getThreadFactory(FACTORY_NAME));
		executorService.scheduleWithFixedDelay(new EasyPreSender(config),lingerMs,lingerMs, TimeUnit.MILLISECONDS);
		LOGGER.info("EasyPre Timer init finish");
	}
}

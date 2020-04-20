package com.easypre.client.send;

import com.easypre.client.config.EasyPreConfig;
import com.easypre.client.model.Request;
import com.easypre.client.model.RequestBuilder;
import com.easypre.client.model.event.BaseEvent;
import com.easypre.client.queue.EventQueue;
import com.easypre.client.util.EncryptUtil;
import com.easypre.client.util.HttpUtil;
import com.easypre.client.util.JsonUtil;
import com.easypre.client.util.RetryUtil;
import com.github.rholder.retry.RetryException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class EasyPreSender implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(EasyPreSender.class);

	private static final String FIELD_STATUS="status";
	private static final String FIELD_MESSAGE="message";
	private static final int STATUS_SUCCESS=200;
	private EasyPreConfig config;

	public EasyPreSender(EasyPreConfig config) {
		this.config = config;
	}

	@Override
	public void run() {
		List<BaseEvent> baseEvents = EventQueue.getWaitPushDataList();
		if (baseEvents.size() > 0) {
			Request<BaseEvent> request = buildRequest(baseEvents);
			try {
				RetryUtil.retry(new Callable() {
					@Override
					public Object call() throws Exception {
						String jsonStr = JsonUtil.beanToJsonStr(request);
						LOGGER.info("推送事件数据：{}", jsonStr);
						String resStr = HttpUtil.postJson(config.getServer(), jsonStr,config.getProxyIp(),config.getProxyPort());
						LOGGER.info("推送事件数据结果：{}", resStr);
						if (StringUtils.isBlank(resStr)){
							throw new RetryUtil.NeedRetryException("推送事件结果错误为空，重试中");
						}
						Map<String,Object> resMap=JsonUtil.jsonStrToBean(resStr,Map.class);
						if (resMap.containsKey(FIELD_STATUS) && STATUS_SUCCESS!=(int)resMap.get(FIELD_STATUS)){
							LOGGER.error("推送事件错误，响应码：{},响应消息：{}",resMap.get(FIELD_STATUS),resMap.get(FIELD_MESSAGE));
							throw new RetryUtil.NeedRetryException("推送事件结果错误："+resMap.get(FIELD_MESSAGE));
						}
						return resStr;
					}
				});
			} catch (ExecutionException e) {
				LOGGER.error("执行任务异常",e);
			} catch (RetryException e) {
				LOGGER.error("重试异常",e);
			}
		}

	}

	private Request<BaseEvent> buildRequest(List<BaseEvent> baseEvents) {
		long timestamp=System.currentTimeMillis();
		String sign= EncryptUtil.encryptPassword(String.valueOf(timestamp),config.getSecret());
		return new RequestBuilder<BaseEvent>()
				.env(config.getEnv())
				.appKey(config.getAppKey())
				.sign(sign)
				.timestamp(timestamp)
				.eventLogs(baseEvents)
				.build();
	}
}

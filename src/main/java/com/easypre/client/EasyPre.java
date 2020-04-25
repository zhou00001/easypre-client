package com.easypre.client;

import com.easypre.client.config.EasyPreConfig;
import com.easypre.client.enums.EventTypeEnum;
import com.easypre.client.exception.EasyPreException;
import com.easypre.client.model.event.BaseEvent;
import com.easypre.client.model.event.NormalEvent;
import com.easypre.client.model.event.TemplateEvent;
import com.easypre.client.queue.EventQueue;
import com.easypre.client.send.EasyPreTimer;
import com.easypre.client.util.ExceptionUtil;
import com.easypre.client.util.JsonUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class EasyPre {
	private static final Logger LOGGER = LoggerFactory.getLogger(EasyPre.class);
	/**
	 * 配置
	 */
	private static EasyPreConfig config;

	private EasyPre() {
	}

	/**
	 * EasyPre初始化
	 *
	 * @param config
	 */
	public static void init(EasyPreConfig config) {
		Preconditions.checkNotNull(config);
		if (!config.isConfigAll()) {
			throw new EasyPreException("EasyPre配置不完整，请检查配置[" + JsonUtil.beanToJsonStr(config) + "]");
		}
//		System.setProperty("log4j.configurationFile", "log4j2.xml");
		EasyPre.config = config;
		EasyPreTimer.init(config);
	}

	/**
	 * 模板事件
	 * @param tag
	 * @param params
	 */
	public static void eventTemplate(String tag, Map<String,Object> params){
		if (!checkConfig()) {
			throw new EasyPreException("EasyPre尚未配置，请检查");
		}
		BaseEvent event = buildEvent(tag, params);
		EventQueue.queue(event);
	}
	/**
	 * 普通事件
	 *
	 * @param tag     标签
	 * @param content 内容
	 * @param params  参数
	 */
	public static void eventTag(String tag, String content, final Object... params) {
		if (!checkConfig()) {
			throw new EasyPreException("EasyPre尚未配置，请检查");
		}
		BaseEvent event = buildEvent(tag,content, params);
		EventQueue.queue(event);
	}

	/**
	 * 内容事件
	 *
	 * @param content 内容
	 * @param params  参数
	 */
	public static void event(String content, final Object... params) {
		eventTag(null,content,params);
	}

	/**
	 * 标签
	 */
	public static void tag(){

	}

	/**
	 * 事务
	 */
	public static void trans(){

	}
	/**
	 * 构建模板事件
	 *
	 * @param tag
	 * @param params
	 * @return
	 */
	private static BaseEvent buildEvent(String tag, Map<String,Object> params) {
		TemplateEvent eventWarn = new TemplateEvent();
		eventWarn.setSign(tag);
		eventWarn.setType(EventTypeEnum.TEMPLATE.getCode());
		eventWarn.setParams(params);
		eventWarn.setOrigTime(LocalDateTime.now());
		eventWarn.setCount(1);
		return eventWarn;
	}
	/**
	 * 构建普通事件
	 *
	 * @param tag
	 * @param content
	 * @param params
	 * @return
	 */
	private static BaseEvent buildEvent(String tag, String content, final Object... params) {
		FormattingTuple formattingTuple = MessageFormatter.arrayFormat(content, params);
		NormalEvent eventWarn = new NormalEvent();
		eventWarn.setSign(tag);
		eventWarn.setType(EventTypeEnum.WARN.getCode());
		eventWarn.setTitle(formattingTuple.getMessage());
		eventWarn.setContent(ExceptionUtil.getExceptionMsg(formattingTuple.getThrowable()));
		eventWarn.setOrigTime(LocalDateTime.now());
		eventWarn.setCount(1);
		return eventWarn;
	}
	/**
	 * 校验配置
	 *
	 * @return
	 */
	private static boolean checkConfig() {
		if (config == null) {
			return false;
		}
		return config.isConfigAll();
	}
}

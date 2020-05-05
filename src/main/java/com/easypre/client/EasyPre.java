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
import org.apache.commons.lang3.StringUtils;
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
		EasyPre.config = config;
		EasyPreTimer.init(config);
	}

	/**
	 * 模板事件
	 *
	 * @param tag    标签
	 * @param params 模板参数
	 */
	public static void eventTemplate(String tag, Map<String, Object> params) {
		eventTemplate(tag, null, params);
	}

	/**
	 * 模板事件
	 *
	 * @param tag    标签
	 * @param to     接收人
	 * @param params 模板参数
	 */
	public static void eventTemplate(String tag, String to, Map<String, Object> params) {
		if (!checkConfig()) {
			throw new EasyPreException("EasyPre尚未配置，请检查");
		}
		BaseEvent event = buildEvent(tag, to, params);
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
		eventTag(tag, null, content, params);
	}

	/**
	 * 普通事件
	 *
	 * @param tag     标签
	 * @param to      接收人
	 * @param content 内容
	 * @param params  参数
	 */
	public static void eventTag(String tag, String to, String content, final Object... params) {
		eventTag(tag, to, null, content, params);
	}

	/**
	 * 普通事件
	 *
	 * @param tag     标签
	 * @param to      接收人
	 * @param title   标题
	 * @param content 内容
	 * @param params  参数
	 */
	public static void eventTag(String tag, String to, String title, String content, final Object... params) {
		if (!checkConfig()) {
			throw new EasyPreException("EasyPre尚未配置，请检查");
		}
		BaseEvent event = buildEvent(tag, to, title, content, params);
		EventQueue.queue(event);
	}

	/**
	 * 内容事件
	 *
	 * @param content 内容
	 * @param params  参数
	 */
	public static void event(String content, final Object... params) {
		eventTag(null, null, content, params);
	}

	/**
	 * 构建模板事件
	 *
	 * @param tag    标签
	 * @param params 参数
	 * @return
	 */
	private static BaseEvent buildEvent(String tag, String to, Map<String, Object> params) {
		TemplateEvent eventWarn = new TemplateEvent();
		eventWarn.setSign(tag);
		eventWarn.setType(EventTypeEnum.TEMPLATE_EVENT.getCode());
		eventWarn.setTo(to);
		eventWarn.setParams(params);
		eventWarn.setOrigTime(LocalDateTime.now());
		eventWarn.setCount(1);
		return eventWarn;
	}

	/**
	 * 构建普通事件
	 *
	 * @param tag     标签
	 * @param title   标题
	 * @param content 内容
	 * @param params  内容参数
	 * @return
	 */
	private static BaseEvent buildEvent(String tag, String to, String title, String content, final Object... params) {
		FormattingTuple formattingTuple = MessageFormatter.arrayFormat(content, params);
		NormalEvent eventWarn = new NormalEvent();
		eventWarn.setSign(tag);
		eventWarn.setType(EventTypeEnum.NORMAL_EVENT.getCode());
		eventWarn.setTo(to);
		eventWarn.setTitle(StringUtils.isBlank(title) ? formattingTuple.getMessage() : title);
		eventWarn.setContent(String.format("%s%s", formattingTuple.getMessage()
				, formattingTuple.getThrowable() != null ? "," + ExceptionUtil.getExceptionMsg(formattingTuple.getThrowable()) : ""));
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

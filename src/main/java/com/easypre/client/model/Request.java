package com.easypre.client.model;

import com.easypre.client.model.event.BaseEvent;

import java.util.List;

/**
 * 请求类
 * @author zhoudc
 * @version 1.0
 * @since 1.0
 */
public class Request<T extends BaseEvent> {
	private Integer type;
	/** 应用key */
	private String appKey;
	/** 时间戳 */
	private Long timestamp;
	/** 请求签名 */
	private String sign;
	/** 所在环境 */
	private String env;
	/** 事件 */
	private List<T> eventLogs;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public List<T> getEventLogs() {
		return eventLogs;
	}

	public void setEventLogs(List<T> eventLogs) {
		this.eventLogs = eventLogs;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Request{" +
				"type=" + type +
				", appKey='" + appKey + '\'' +
				", timestamp='" + timestamp + '\'' +
				", sign='" + sign + '\'' +
				", env='" + env + '\'' +
				", eventLogs=" + eventLogs +
				'}';
	}
}

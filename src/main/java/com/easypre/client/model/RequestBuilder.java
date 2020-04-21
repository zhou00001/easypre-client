package com.easypre.client.model;

import com.easypre.client.enums.EventTypeEnum;
import com.easypre.client.model.event.BaseEvent;

import java.util.List;

/**
 * 请求构建类
 * @author zhoudc
 * @version 1.0
 * @since 1.0
 */
public class RequestBuilder<T extends BaseEvent> {
	private Request<T> request;

	public RequestBuilder() {
		this.request = new Request<>();
		this.request.setType(EventTypeEnum.WARN.getCode());
	}
	public RequestBuilder<T> type(EventTypeEnum eventTypeEnum){
		request.setType(eventTypeEnum.getCode());
		return this;
	}
	public RequestBuilder<T> env(String env){
		request.setEnv(env);
		return this;
	}
	public RequestBuilder<T> appKey(String appKey){
		request.setAppKey(appKey);
		return this;
	}
	public RequestBuilder<T> sign(String sign){
		request.setSign(sign);
		return this;
	}
	public RequestBuilder<T> timestamp(Long timestamp){
		request.setTimestamp(timestamp);
		return this;
	}
	public RequestBuilder<T> eventLogs(List<T> eventLogs){
		request.setEventLogs(eventLogs);
		return this;
	}
	public Request<T> build(){
		return request;
	}
}

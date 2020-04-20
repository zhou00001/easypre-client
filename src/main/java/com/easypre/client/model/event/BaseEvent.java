package com.easypre.client.model.event;

import java.time.LocalDateTime;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseEvent implements Event {
	/** 事件类型 */
	private Integer type;
	/** 原始时间 */
	private LocalDateTime origTime;
	/** 签名 */
	private String sign;
	/** 事件次数 */
	private Integer count;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalDateTime getOrigTime() {
		return origTime;
	}

	public void setOrigTime(LocalDateTime origTime) {
		this.origTime = origTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "BaseEvent{" +
				"type=" + type +
				", origTime=" + origTime +
				", sign='" + sign + '\'' +
				", count=" + count +
				'}';
	}
}

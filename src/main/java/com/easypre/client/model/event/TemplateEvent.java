package com.easypre.client.model.event;

import com.easypre.client.util.EncryptUtil;

import java.util.Map;

/**
 * 模板事件
 * @author zhoudecai
 * @version 1.0
 * @since
 */
public class TemplateEvent extends BaseEvent {
	private Map<String,Object> params;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	@Override
	public String getSign() {
		if (super.getSign()==null) {
			setSign(EncryptUtil.hashNoEncryptToStr(String.format("%s-%s", getType(), getParams())));
		}
		return super.getSign();
	}

	@Override
	public String toString() {
		return "TemplateEvent{" +
				"params=" + params +
				"} " + super.toString();
	}
}

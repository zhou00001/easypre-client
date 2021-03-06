package com.easypre.client.enums;

/**
 * 事件类型枚举
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public enum EventTypeEnum {
	/** 普通事件 */
	NORMAL_EVENT(13601,"普通事件"),
	/** 模板事件 */
	TEMPLATE_EVENT(13602,"模板事件"),
	;
	/** 编码 */
	private Integer code;
	/** 名称 */
	private String name;

	EventTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * 根据编码匹配枚举
	 * @param code
	 * @return
	 */
	public static EventTypeEnum of(Integer code){
		if (code==null){
			return null;
		}
		for (EventTypeEnum itemEnum:EventTypeEnum.values()){
			if (itemEnum.getCode().equals(code)){
				return itemEnum;
			}
		}
		return null;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "EventTypeEnum{" +
				"code=" + code +
				", name='" + name + '\'' +
				"} " + super.toString();
	}
}

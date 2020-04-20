package com.easypre.client.model.event;

import com.easypre.client.util.EncryptUtil;

/**
 * 事件
 *
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class WarnEvent extends BaseEvent {
	/**
	 * 事件标题
	 */
	private String title;
	/**
	 * 事件内容
	 */
	private String content;

	@Override
	public String getSign() {
		if (super.getSign()==null) {
			setSign(EncryptUtil.hashNoEncryptToStr(String.format("%s-%s-%s", getType(), getTitle(), getContent())));
		}
		return super.getSign();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public String toString() {
		return "WarnEvent{" +
				"title='" + title + '\'' +
				", content='" + content + '\'' +
				"} " + super.toString();
	}
}

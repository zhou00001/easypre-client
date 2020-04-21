package com.easypre.client.queue;

import com.easypre.client.model.event.BaseEvent;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoudc
 * @version 1.0
 * @since 1.0
 */
public class EventQueue {
	private static ConcurrentHashMap<String, List<BaseEvent>> eventMap = new ConcurrentHashMap<>();

	/**
	 * 待推送数据进入队列
	 *
	 * @param event
	 * @return
	 */
	public static boolean queue(BaseEvent event) {
		String sign = event.getSign();
		List<BaseEvent> baseEvents = eventMap.getOrDefault(sign, Lists.newArrayList());
		baseEvents.add(event);
		eventMap.put(sign, baseEvents);
		return true;
	}

	/**
	 * 获取等待推送的数据
	 *
	 * @return
	 */
	public static List<BaseEvent> getWaitPushDataList() {
		if (eventMap.isEmpty()) {
			return Lists.newArrayList();
		}
		List<BaseEvent> baseEvents = Lists.newArrayList();
		for (Map.Entry<String, List<BaseEvent>> entry : eventMap.entrySet()) {
			if (entry.getValue() != null && entry.getValue().size() > 0) {
				BaseEvent baseEvent = entry.getValue().get(0);
				baseEvent.setCount(entry.getValue().size());
				baseEvents.add(baseEvent);
			}
		}
		eventMap.clear();
		return baseEvents;
	}
}

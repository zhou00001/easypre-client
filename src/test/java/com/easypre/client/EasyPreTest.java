package com.easypre.client;

import com.easypre.client.config.EasyPreConfig;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoudecai
 * @version 1.0
 * @date 2020-03-26
 */
public class EasyPreTest {
	@Before
	public void init() {
		EasyPre.init(new EasyPreConfig("http://127.0.0.1:9104/api/open/easypre/event", "pro", "test", "test"));
	}

	@Test
	public void warn() throws InterruptedException {
		int i = 10;
		while (i > 0) {
			EasyPre.eventTag("test", "测试:{}", LocalDate.now().toString(), new RuntimeException("测试异常"));
			i--;
		}
		TimeUnit.SECONDS.sleep(20);
	}

	@Test
	public void event() throws InterruptedException {

		EasyPre.event("测试:{}", LocalDate.now().toString(), new RuntimeException("测试异常"));

		TimeUnit.SECONDS.sleep(10);
	}

	@Test
	public void warnEmail() throws InterruptedException {

//		EasyPre.eventTag("email", "测试:{}", LocalDate.now().toString(), new RuntimeException("测试异常"));
		Map<String, Object> map = Maps.newHashMap();
		map.put("name", "zhou");
		EasyPre.eventTemplate("email", null, map);
		TimeUnit.SECONDS.sleep(10);
	}

	@Test
	public void warnTemplate() throws InterruptedException {
//		while (true) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "张三");
		params.put("code", "232322");
		params.put("miniute", 5);
		EasyPre.eventTemplate("template", "zhou00001@126.com", params);
		TimeUnit.SECONDS.sleep(10);
//		}
	}

	@Test
	public void warnTemplate2() throws InterruptedException {
		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "张三");
		params.put("code", "232322");
		params.put("miniute", 5);
		EasyPre.eventTemplate("email", "zhou00001@126.com", params);
		TimeUnit.SECONDS.sleep(10);
	}
}
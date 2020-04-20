package com.easypre.client;

import com.easypre.client.config.EasyPreConfig;
import org.junit.Test;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoudecai
 * @version 1.0
 * @date 2020-03-26
 */
public class EasyPreTest {

	@Test
	public void warn() throws InterruptedException {
		EasyPre.init(new EasyPreConfig("http://127.0.0.1:9104/api/open/easypre/event", "pro", "test", "test"));

//		while (true) {
			EasyPre.warnTag("test", "测试:{}", LocalDate.now().toString(), new RuntimeException("测试异常"));
			TimeUnit.MILLISECONDS.sleep(200);
//		}
	}

	@Test
	public void warnEmail() throws InterruptedException {
		EasyPre.init(new EasyPreConfig("http://127.0.0.1:9104/api/open/easypre/event", "pro", "test", "test"));

		EasyPre.warnTag("email", "测试:{}", LocalDate.now().toString(), new RuntimeException("测试异常"));
		TimeUnit.SECONDS.sleep(10);
	}
}
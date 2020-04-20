package com.easypre.client.util;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhoudecai
 * @version 1.0
 * @date 2020-03-28
 */
public class EncryptUtilTest {

	@Test
	public void hash() throws InterruptedException {
		Map<String,String> set= Maps.newHashMap();
		int count=100;
		long now=System.currentTimeMillis();
		System.out.println("开始");
		CountDownLatch countDownLatch=new CountDownLatch(count);
		for (int i=0;i<count;i++) {
			final int j=i;
			ThreadPoolUtil.execute("test",new Runnable() {
				@Override
				public void run() {
					String str=UUID.randomUUID().toString();
					String hash=EncryptUtil.hashNoEncryptToStr(str);
					if (set.containsKey(hash)){
						System.out.println("repeat hashNoEncrypt:"+hash+",str:"+str+",repeatStr:"+set.get(hash));
					}
					if (j%1000==0){
						System.out.println("i:"+j+",str:"+str+",hashNoEncrypt:"+hash);
					}
					set.put(hash,str);
					countDownLatch.countDown();
				}
			},ThreadPoolUtil.AVAILABLE_PROCESSORS,ThreadPoolUtil.AVAILABLE_PROCESSORS *10);
		}
		countDownLatch.await();
		System.out.println("结束，耗时："+(System.currentTimeMillis()-now)+" ms");
	}
	private String buildStr(){
		String str="";
		for (int i=0;i<10;i++){
			str+=UUID.randomUUID().toString();
		}
		return str;
	}
}
package com.easypre.client.util;

import com.github.rholder.retry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class RetryUtil {
	private static final Logger LOGGER= LoggerFactory.getLogger(RetryUtil.class);
	private static Retryer<Object> retryer= RetryerBuilder.newBuilder()
			.retryIfExceptionOfType(NeedRetryException.class)
			.withWaitStrategy(WaitStrategies.incrementingWait(50, TimeUnit.MILLISECONDS,200,TimeUnit.MILLISECONDS))
			.withStopStrategy(StopStrategies.stopAfterAttempt(5))
			.withRetryListener(new RetryListener() {
				@Override
				public <V> void onRetry(Attempt<V> attempt) {
					if (attempt.hasException()){
						LOGGER.info("异常重试中，当前重试次数：{}",attempt.getAttemptNumber(),attempt.getExceptionCause());
					}

				}
			})
			.build();

	/**
	 * 需要重试的异常
	 */
	public static class NeedRetryException extends RuntimeException{
		public NeedRetryException() {
		}

		public NeedRetryException(String message) {
			super(message);
		}

		public NeedRetryException(String message, Throwable cause) {
			super(message, cause);
		}

		public NeedRetryException(Throwable cause) {
			super(cause);
		}

		public NeedRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}
	}

	/**
	 * 重复执行任务
	 * @param callable
	 * @param <T>
	 * @return
	 * @throws ExecutionException
	 * @throws RetryException
	 */
	public static  <T> T retry(Callable callable) throws ExecutionException, RetryException {
		return (T)retryer.call(callable);
	}
}

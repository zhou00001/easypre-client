package com.easypre.client.exception;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class EasyPreException extends RuntimeException {
	public EasyPreException() {
	}

	public EasyPreException(String message) {
		super(message);
	}

	public EasyPreException(String message, Throwable cause) {
		super(message, cause);
	}

	public EasyPreException(Throwable cause) {
		super(cause);
	}

	public EasyPreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

package com.easypre.client.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 网络请求工具类
 *
 * @author zhoudc
 * @since 1.0
 */
public class HttpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * 普通连接
	 */
	public static final int DEFAULT_CONNECT_TIMEOUT = 8;
	public static final int DEFAULT_READ_TIMEOUT = 30;
	public static final int DEFAULT_WRITE_TIMEOUT = 30;
	/**
	 * 长连接
	 */
	public static final int DEFAULT_LONG_CONNECT_TIMEOUT = 60;
	public static final int DEFAULT_LONG_READ_TIMEOUT = 1800;
	public static final int DEFAULT_LONG_WRITE_TIMEOUT = 1800;
	/**
	 * MEDIA_TYPE_JSON
	 */
	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	/**
	 * 普通连接客户端
	 */
	public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
			.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
			.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
			.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
			.retryOnConnectionFailure(true)
			.build();
	/**
	 * 长连接客户端
	 */
	public static OkHttpClient longOkHttpClient = new OkHttpClient.Builder()
			.connectTimeout(DEFAULT_LONG_CONNECT_TIMEOUT, TimeUnit.SECONDS)
			.readTimeout(DEFAULT_LONG_READ_TIMEOUT, TimeUnit.SECONDS)
			.writeTimeout(DEFAULT_LONG_WRITE_TIMEOUT, TimeUnit.SECONDS)
			.retryOnConnectionFailure(true)
			.build();

	private HttpUtil() {
	}

	/**
	 * GET对象
	 *
	 * @param url
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getObject(String url, Class<T> clazz) {
		String result = get(url, null);
		T t = JsonUtil.jsonStrToBean(result, clazz);
		return t;
	}

	/**
	 * GET对象
	 *
	 * @param url
	 * @param params
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getObject(String url, Map<String, Object> params, Class<T> clazz) {
		String result = get(url, params);
		T t = JsonUtil.jsonStrToBean(result, clazz);
		return t;
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String get(String url, Map<String, Object> params) {
		return get(okHttpClient, buildUrl(url, params));
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @return
	 */
	public static String get(String url) {
		return get(okHttpClient, url);
	}

	/**
	 * Json数据提交
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postJson(String url, Map<String, Object> params) {
		return postJson(url, JsonUtil.beanToJsonStr(params));
	}

	/**
	 * Json数据提交
	 *
	 * @param url
	 * @param jsonStr
	 * @return
	 */
	public static String postJson(String url, String jsonStr) {
		return postJson(url, jsonStr, null, null);
	}

	/**
	 * Json数据提交
	 *
	 * @param url
	 * @param jsonStr
	 * @return
	 */
	public static String postJson(String url, String jsonStr, String proxyIp, Integer proxyPort) {
		RequestBody requestBody = RequestBody.create(jsonStr, MEDIA_TYPE_JSON);
		Request request = new Request
				.Builder()
				.url(url)
				.post(requestBody)
				.build();
		try {
			OkHttpClient usedClient = okHttpClient;
			if (StringUtils.isNotBlank(proxyIp) && proxyPort != null && proxyPort > 0) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
				usedClient = new OkHttpClient.Builder()
						.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
						.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
						.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
						.retryOnConnectionFailure(true)
						.proxy(proxy)
						.build();
			}
			Response response = usedClient.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().string();
			} else {
				LOGGER.error("请求失败,url:{},json:{},status:{}", url, jsonStr, response.code());
			}
		} catch (IOException e) {
			LOGGER.error("请求异常,{},json:{}", url, jsonStr, e);
			return null;
		}
		return null;
	}

	/**
	 * 长请求
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getLong(String url, Map<String, Object> params) {
		return get(longOkHttpClient, buildUrl(url, params));
	}

	/**
	 * 长请求
	 *
	 * @param url
	 * @return
	 */
	public static String getLong(String url) {
		return get(longOkHttpClient, url);
	}

	/**
	 * GET同步请求
	 *
	 * @param okHttpClient
	 * @param url
	 * @return
	 */
	public static String get(OkHttpClient okHttpClient, String url) {
		Request request = new Request.Builder().url(url).build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().string();
			} else {
				LOGGER.error("GET请求[{}]失败，返回：{},{}", url, response.code(), response);
			}
		} catch (Exception e) {
			LOGGER.error("GET请求[{}]异常", url, e);
		}
		return null;
	}

	/**
	 * GET异步请求
	 *
	 * @param url
	 * @param parmas
	 * @param callback
	 */
	public static void getAsync(String url, Map<String, Object> parmas, Callback callback) {
		Request request = new Request.Builder().url(buildUrl(url, parmas)).build();
		longOkHttpClient.newCall(request).enqueue(callback);
	}

	/**
	 * GET异步请求
	 *
	 * @param url
	 */
	public static void getAsync(String url, Callback callback) {
		Request request = new Request.Builder().url(url).build();
		longOkHttpClient.newCall(request).enqueue(callback);
	}

	/**
	 * post请求
	 *
	 * @param url
	 * @param formBody
	 * @return
	 */
	public static String post(String url, FormBody formBody) {
		return post(okHttpClient, url, formBody);
	}

	/**
	 * 长post请求
	 *
	 * @param url
	 * @param formBody
	 * @return
	 */
	public static String postLong(String url, FormBody formBody) {
		return post(longOkHttpClient, url, formBody);
	}

	/**
	 * POST请求
	 *
	 * @param okHttpClient
	 * @param url
	 * @param formBody
	 * @return
	 */
	public static String post(OkHttpClient okHttpClient, String url, FormBody formBody) {
		Request request = new Request.Builder().url(url).post(formBody).build();
		try {
			Response response = okHttpClient.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().string();
			} else {
				LOGGER.error("GET请求[{}]失败，返回：{},{}", url, response.code(), response);
			}
		} catch (Exception e) {
			LOGGER.error("GET请求[{}]异常", url, e);
		}
		return null;
	}

	/**
	 * 异步请求
	 *
	 * @param url
	 * @param formBody
	 * @param callback
	 */
	public static void postAsync(String url, FormBody formBody, Callback callback) {
		Request request = new Request.Builder().url(url).post(formBody).build();
		longOkHttpClient.newCall(request).enqueue(callback);
	}

	/**
	 * 快速构建单个参数的url
	 *
	 * @param url
	 * @param key
	 * @param value
	 * @return
	 */
	public static String buildUrl(String url, String key, Object value) {
		Map<String, Object> params = Maps.newHashMap();
		params.put(key, value);
		return buildUrl(url, params);
	}

	/**
	 * 构建url
	 *
	 * @param url    原url
	 * @param params 参数
	 * @return
	 */
	public static String buildUrl(String url, Map<String, Object> params) {
		if (StringUtils.isBlank(url)) {
			LOGGER.error("url为空");
			return null;
		}
		if (params == null || params.size() == 0) {
			return url;
		}
		StringBuilder stringBuilder = new StringBuilder();
		String paramStr = Joiner.on("&").join(params.entrySet().stream().map(entry -> {
			try {
				return String.format("%s=%s", entry.getKey(), URLEncoder.encode(StringUtil.convertToStr(entry.getValue()),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("不支持的编码",e);
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList()));
		if (url.indexOf("?") >= 0) {
			return url + "&" + paramStr;
		} else {
			return url + "?" + paramStr;
		}
	}


}

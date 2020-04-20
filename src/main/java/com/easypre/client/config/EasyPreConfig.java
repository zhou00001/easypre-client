package com.easypre.client.config;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhoudecai
 * @version 1.0
 * @since 1.0
 */
public class EasyPreConfig {
	/** 服务接收地址 */
	private String server;
	/** 当前环境 */
	private String env;
	/** 应用key */
	private String appKey;
	/** 应用密钥 */
	private String secret;
	/** 重试次数 */
	private Integer retries;
	/** 每次批量发送数量 */
	private Integer batchSize;
	/** 延迟发送时长（ms） */
	private Integer lingerMs;
	/** 代理发送ip */
	private String proxyIp;
	/** 代理发送端口 */
	private Integer proxyPort;

	public EasyPreConfig() {
	}

	public EasyPreConfig(String server, String env, String appKey, String secret) {
		this.server = server;
		this.env = env;
		this.appKey = appKey;
		this.secret = secret;
	}

	/**
	 * 检查配置是否完整
	 * @return
	 */
	public boolean isConfigAll(){
		return !StringUtils.isAnyBlank(server,env,appKey,secret);
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public Integer getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Integer batchSize) {
		this.batchSize = batchSize;
	}

	public Integer getLingerMs() {
		return lingerMs;
	}

	public void setLingerMs(Integer lingerMs) {
		this.lingerMs = lingerMs;
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	@Override
	public String toString() {
		return "EasyPreConfig{" +
				"server='" + server + '\'' +
				", env='" + env + '\'' +
				", appKey='" + appKey + '\'' +
				", secret='" + secret + '\'' +
				", retries=" + retries +
				", batchSize=" + batchSize +
				", lingerMs=" + lingerMs +
				", proxyIp='" + proxyIp + '\'' +
				", proxyPort=" + proxyPort +
				'}';
	}
}

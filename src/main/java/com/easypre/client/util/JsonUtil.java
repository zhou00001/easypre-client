package com.easypre.client.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Json相关工具类
 * 
 * @author zhoudc
 * @since 1.0
 */
public class JsonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper objectMapper;

	private JsonUtil() {
	}

	/**
	 * 对象转换为json字符串 对象为空或转换失败，返回{}
	 * 
	 * @param bean
	 *            对象
	 * @return json字符串
	 */
	public static String beanToJsonStr(Object bean) {
		if (bean == null) {
			LOGGER.error("传入参数为空");
			return "{}";
		}
		ObjectMapper mapper = getObjectMapper();
		try {
			String jsonStr = mapper.writeValueAsString(bean);
			return jsonStr;
		} catch (JsonProcessingException e) {
			LOGGER.error("字符串转换失败,bean:{}", bean, e);
			return "{}";
		}
	}

	/**
	 * json字符串转换为对象
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @param clazz
	 *            对象类型
	 * @param <T>
	 * @return
	 */
	public static <T> T jsonStrToBean(String jsonStr, Class<T> clazz) {
		ObjectMapper mapper = getObjectMapper();
		try {
			return (T) mapper.readValue(jsonStr, clazz);
		} catch (IOException e) {
			LOGGER.error("json字符串转对象失败,jsonStr:{},clazz:{}", jsonStr, clazz, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * json字符串转换为对象
	 * 
	 * @param jsonStr
	 *            json字符串
	 * @param valueTypeRef
	 *            对象类型引用
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T jsonStrToBean(String jsonStr, TypeReference<T> valueTypeRef) {
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		ObjectMapper mapper = getObjectMapper();
		try {
			return (T) mapper.readValue(jsonStr, valueTypeRef);
		} catch (IOException e) {
			LOGGER.error("json字符串转对象失败,jsonStr:{},valueTypeRef:{}", jsonStr, valueTypeRef, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取ObjectMapper
	 * @return
	 */
	private static ObjectMapper getObjectMapper(){
		if (objectMapper!=null){
			return objectMapper;
		}
		synchronized (JsonUtil.class) {
			if (objectMapper!=null){
				return objectMapper;
			}
			LOGGER.info("jackson初始化配置开始");
			objectMapper = new ObjectMapper();
			// 对于空的对象转json的时候不抛出错误
			objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
			// 禁用遇到未知属性抛出异常
			objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			// 序列化BigDecimal时不使用科学计数法输出
			objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(format);
			// 日期和时间格式化
			JavaTimeModule javaTimeModule = new JavaTimeModule();
			javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
			javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));

			objectMapper.registerModule(javaTimeModule);
			LOGGER.info("jackson初始化配置成功");
		}
		return objectMapper;
	}
}

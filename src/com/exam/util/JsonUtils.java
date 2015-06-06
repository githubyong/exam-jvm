package com.exam.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	private static Logger logger = LogManager.getLogger(JsonUtils.class);

	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_DEFAULT);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		DateFormat dataFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
		mapper.setDateFormat(dataFormat);
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		mapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
	}

	public static <T> T parse(String json, Class<T> clazz) {
		if (isJsonEmpty(json))
			return null;

		try {
			return mapper.readValue(json, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + json, e);
			return null;
		}
	}

	public static <T> T parse(String json, TypeReference<T> valueTypeRef) {
		if (isJsonEmpty(json))
			return null;

		try {
			return mapper.readValue(json, valueTypeRef);
		} catch (IOException e) {
			logger.warn("parse json string error:" + json, e);
			return null;
		}
	}

	public static String print(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	public static String printPretty(Object object) {
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	private static boolean isJsonEmpty(String json) {
		if (json == null)
			return true;
		return false;
	}

}

package com.exam.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	final ObjectMapper defaultObjectMapper;

	public MyObjectMapperProvider() {
		defaultObjectMapper = createDefaultMapper();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return defaultObjectMapper;
	}

	private static ObjectMapper createDefaultMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_DEFAULT);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		DateFormat dataFormat = new SimpleDateFormat(DATE_FORMAT,
				Locale.ENGLISH);
		mapper.setDateFormat(dataFormat);
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		mapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);

		return mapper;
	}

}
package com.exam;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.exam.util.MyObjectMapperProvider;

@ApplicationPath("resources")
public class ExamApplication extends ResourceConfig {
	public ExamApplication() {
		packages("com.exam.resource;");
		this.register(MyObjectMapperProvider.class);
		this.register(AuthorizationRequestFilter.class);
	}
}
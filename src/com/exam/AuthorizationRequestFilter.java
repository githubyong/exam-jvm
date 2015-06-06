package com.exam;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;

@PreMatching
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {
	private static Logger logger = LogManager.getLogger(AuthorizationRequestFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authHeader = requestContext.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		boolean authoritiy = false;
		if (authHeader != null) {
			String decoded = new String(Base64.decode(authHeader.substring(6).getBytes()), Charset.forName("utf-8"));

			logger.info(" decoded auth = " + decoded);
			if (StringUtils.equals(decoded, "admin:123"))
				authoritiy = true;
		}
		if (!authoritiy)
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
	}

}

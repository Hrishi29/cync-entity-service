package com.idexcel.cync.los.entity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.interceptors.InitialHttpRequestInterceptor;

@Configuration
public class EntityConfig implements WebMvcConfigurer {

	private static final Logger LOG = LoggerFactory.getLogger(EntityConfig.class);
	private final InitialHttpRequestInterceptor initialLogInInterceptor;

	@Autowired
	public EntityConfig(InitialHttpRequestInterceptor initialLogInInterceptor) {
		this.initialLogInInterceptor = initialLogInInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(initialLogInInterceptor).addPathPatterns("/" + CoreConstants.API_BASE_PATH + "/**");
		LOG.debug(" Registered InitialLogInInterceptor ");
	}

	/**
	 * Enabling Global CORS -- individual mappings are not enough to handle 401
	 * headers with valid cors as its happening before reaching to any controllers.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}
}

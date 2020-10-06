package com.idexcel.cync.los.entity.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class to add swagger for all the LOS Entity Rest APIs
 * 
 * @author IDEXCEL
 *
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@PropertySource("classpath:swagger.properties")
@ConfigurationProperties("swagger")
@Getter
@Setter
public class SwaggerConfig {

	private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfig.class);
	private final List<SwaggerSpecs> specs = new ArrayList<>();
	private final ConfigurableApplicationContext context;
	private static final String ENTITY_BASE_PACKAGE = "com.idexcel.cync.los.entity";

	/**
	 * Global HTTP Response Messages
	 */
	final List<ResponseMessage> globalResponses = Arrays.asList(
			new ResponseMessageBuilder().code(200).message("OK").build(),
			new ResponseMessageBuilder().code(201).message("Resource Created").build(),
			new ResponseMessageBuilder().code(204).message("No Content").build(),
			new ResponseMessageBuilder().code(404).message("Resource Not Found").build(),
			new ResponseMessageBuilder().code(400).message("Bad Request").build(),
			new ResponseMessageBuilder().code(500).message("System Encountered Error").build());

	@Data
	public static class SwaggerSpecs {
		private String name;
		private String host;
		private String protocal;

		@Override
		public String toString() {
			return "SwaggerSpecs [name=" + name + ", host=" + host + "]";
		}
	}

	@Autowired
	public SwaggerConfig(ConfigurableApplicationContext context) {
		super();
		this.context = context;
	}

	@PostConstruct
	public void createDockets() {
		SingletonBeanRegistry beanRegistry = context.getBeanFactory();
		for (SwaggerSpecs swaggerSpec : specs) {
			Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName(swaggerSpec.getName())
					.host(swaggerSpec.getHost()).protocols(Collections.singleton(swaggerSpec.getProtocal()))
					.securitySchemes(Arrays.asList(jwtToken()))
					.securityContexts(Collections.singletonList(securityContext())).useDefaultResponseMessages(false)
					.globalResponseMessage(RequestMethod.GET, globalResponses)
					.globalResponseMessage(RequestMethod.POST, globalResponses)
					.globalResponseMessage(RequestMethod.DELETE, globalResponses)
					.globalResponseMessage(RequestMethod.PATCH, globalResponses)
					.globalResponseMessage(RequestMethod.PUT, globalResponses).select()
					.apis(RequestHandlerSelectors.basePackage(ENTITY_BASE_PACKAGE)).build().apiInfo(getApiInfo());
			beanRegistry.registerSingleton(swaggerSpec.getName() + UUID.randomUUID().toString(), docket);
			LOG.debug(" Creating Swagger for host ==  {}", swaggerSpec.getHost());
		}
	}

	/**
	 * Authentication Token Key
	 * 
	 * @return
	 */
	private ApiKey jwtToken() {
		return new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, In.HEADER.name());
	}

	/**
	 * Basic API Information about CYNC-LOS Entity Microservice
	 * 
	 * @return
	 */
	private ApiInfo getApiInfo() {
		return new ApiInfo("IDEXCEL's CYNC-LOS Entity Microservice API Documention",
				"This document contains all the API details related to Entity Microservice", "1.0",
				"TERMS OF SERVICE URL", new Contact("NDS Systems", "https://cyncsoftware.com/", ""), "LICENSE",
				"LICENSE URL", Collections.emptyList());
	}

	/**
	 * 
	 * @return
	 */
	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
	}

	/**
	 * 
	 * @return
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference(HttpHeaders.AUTHORIZATION, authorizationScopes));
	}
}

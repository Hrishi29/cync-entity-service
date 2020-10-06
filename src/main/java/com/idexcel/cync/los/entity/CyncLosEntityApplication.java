package com.idexcel.cync.los.entity;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class })
@EnableSwagger2
@PropertySource("classpath:swagger.properties")
public class CyncLosEntityApplication {

	public static void main(String[] args) {//NOSONAR
		SpringApplication.run(CyncLosEntityApplication.class, args);
	}

	/**
	 * Message Resource Bean for defining custom messages file path
	 * 
	 * @return messageBundle
	 */
	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
		messageBundle.setBasename("classpath:messages/messages");
		messageBundle.setDefaultEncoding("UTF-8");
		LocaleContextHolder.setLocale(Locale.US);
		return messageBundle;
	}

}

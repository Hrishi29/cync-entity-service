package com.idexcel.cync.los.entity.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Class to add auditing data to los models
 * 
 * @author Idexcel
 *
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(basePackages = { "com.idexcel.cync.los.entity.dao" })
@EntityScan(basePackages = { "com.idexcel.cync.los.entity.model" })
public class JpaConfig {
	
	  @Bean
	    public AuditorAware<String> auditorAware() {
	        return new AuditorAwareImpl();
	    }
}
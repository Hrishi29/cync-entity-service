package com.idexcel.cync.los.entity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import com.idexcel.cync.los.entity.filter.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] WHITE_LISTED_URLS = { "/home/heartBeat", "/home/version", "/actuator/health",
			"/error" };
	private static final String[] SWAGGER_ENDPOINTS = { "/v2/api-docs/**", "/configuration/ui/**",
			"/swagger-resources/**", "/configuration/security/**", "/swagger-ui.html/**", "/webjars/**", "/csrf/**" };

	private final Environment environment;

	@Autowired
	public SecurityConfiguration(Environment environment) {
		this.environment = environment;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().antMatchers("/").permitAll().and()//NOSONAR
				.addFilter(new AuthenticationFilter(authenticationManager(), environment)).csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.httpFirewall(this.defaultHttpFirewall());
		web.ignoring().antMatchers(WHITE_LISTED_URLS);
		for (String activeProfile : this.environment.getActiveProfiles()) {
			if (environment.getProperty("swagger.allowed.profiles").contains(activeProfile)) {
				web.ignoring().antMatchers(SWAGGER_ENDPOINTS);
			}
		}
	}

	@Bean
	public HttpFirewall defaultHttpFirewall() {
		return new DefaultHttpFirewall();
	}
}

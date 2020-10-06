package com.idexcel.cync.los.entity.multitenant;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("spring.multitenancy")
public class MultitenancyProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private LosDataSourceProperties dataSourcesProps;

	public LosDataSourceProperties getDataSources() {
		return this.dataSourcesProps;
	}

	public void setDataSources(LosDataSourceProperties dataSourcesProps) {
		this.dataSourcesProps = dataSourcesProps;
	}

	@Setter
	@Getter
	public static class LosDataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
			implements Serializable {
		private static final long serialVersionUID = 1L;
		private String tenantId;
		private String lender;
		private String timeZone;
		private int connectionPoolSize;
	}
}

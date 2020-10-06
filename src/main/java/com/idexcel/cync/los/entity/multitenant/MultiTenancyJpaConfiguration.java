package com.idexcel.cync.los.entity.multitenant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.idexcel.cync.los.entity.common.constants.CoreConstants;
import com.idexcel.cync.los.entity.multitenant.MultitenancyProperties.LosDataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties({ MultitenancyProperties.class, JpaProperties.class })
@EnableTransactionManagement
public class MultiTenancyJpaConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(MultiTenancyJpaConfiguration.class);

	private final JpaProperties jpaProperties;

	@Autowired
	public MultiTenancyJpaConfiguration(JpaProperties jpaProperties) {
		this.jpaProperties = jpaProperties;
	}

	@Primary
	@Bean(name = "dataSourcesMtApp")
	public Map<String, DataSource> dataSourcesMtApp() {
		return new HashMap<>();
	}

	@Bean
	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		// Autowires dataSourcesMtApp
		return new MultitenantConnectionProvider();
	}

	@Bean
	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		return new CurrentTenantIdentifierResolverImpl();
	}

	@Primary
	@Bean(name = "tenantEntityManager")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
			MultiTenantConnectionProvider multiTenantConnectionProvider,
			CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
		Map<String, Object> hibernateProps = new LinkedHashMap<>();
		hibernateProps.putAll(this.jpaProperties.getProperties());
		hibernateProps.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		hibernateProps.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
				multiTenantConnectionProvider);
		hibernateProps.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
				currentTenantIdentifierResolver);
		hibernateProps.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProps.put(org.hibernate.cfg.Environment.SHOW_SQL, false);
		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
		result.setPackagesToScan(CoreConstants.LOAS_ENTITY_BASE_PACKAGE);
		result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		result.setJpaPropertyMap(hibernateProps);
		LOG.debug("in entityManagerFactoryBean ");
		return result;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
		return entityManagerFactoryBean.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	/**
	 * 
	 * @param dsProperties
	 * @param poolName
	 * @return
	 */
	@SuppressWarnings("unused")
	private HikariDataSource getHikariDatasource(LosDataSourceProperties dsProperties, int poolSize, String poolName) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(dsProperties.getUrl());
		hikariConfig.setUsername(dsProperties.getUsername());
		hikariConfig.setPassword(dsProperties.getPassword());
		hikariConfig.setMaximumPoolSize(poolSize);
		hikariConfig.setPoolName(dsProperties.getLender() + "_" + poolName);
		hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.cacheResultSetMetadata", "true");
		return new HikariDataSource(hikariConfig);
	}
}

package com.idexcel.cync.los.entity.multitenant;

import java.io.Serializable;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import com.idexcel.cync.los.entity.common.constants.LOSEntityConstants;
import com.idexcel.cync.los.entity.exception.BadRequestException;
import com.idexcel.cync.los.entity.multitenant.MultitenancyProperties.LosDataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class MultitenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl implements Serializable{
	private static final long serialVersionUID = 1L;

	@Autowired
	private Map<String, DataSource> dataSourcesMtApp;//NOSONAR
	@Autowired
	private MultitenancyProperties multitenancyProperties;

	@Override
	protected DataSource selectAnyDataSource() {
		return this.dataSourcesMtApp.values().iterator().next();
	}
	
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		DataSource dt = this.dataSourcesMtApp.get(tenantIdentifier);
		LosDataSourceProperties dbProperties =	this.multitenancyProperties.getDataSources();
	    HikariDataSource dataSource = null;
	    String lenderName= MDC.get(LOSEntityConstants.CLIENT_NAME_KEY);
		if (dt == null) {
			try {
				dataSource = getHikariDatasource(dbProperties, lenderName, dbProperties.getConnectionPoolSize(), "_db_connection_pool");
			} catch (Exception e) {
				throw new BadRequestException("application is not configred for lender "+lenderName);
			}
			dataSourcesMtApp.put(lenderName, dataSource);
			return dataSource;
		}
		return dt;
	}
	
	
	/**
	 * 
	 * @param dsProperties
	 * @param poolName
	 * @return
	 */
	private HikariDataSource getHikariDatasource(LosDataSourceProperties dsProperties, String dbName, int poolSize, String poolName) {
		HikariConfig hikariConfig = new HikariConfig();
		String url = dsProperties.getUrl().concat("/").concat(dbName);
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(dsProperties.getUsername());
		hikariConfig.setPassword(dsProperties.getPassword());
		hikariConfig.setMaximumPoolSize(poolSize);
		hikariConfig.setPoolName(dbName + "_" + poolName);
		hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
		hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.cacheResultSetMetadata", "true");
		return new HikariDataSource(hikariConfig);
	}
}

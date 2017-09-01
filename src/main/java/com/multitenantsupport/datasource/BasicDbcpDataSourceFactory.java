package com.multitenantsupport.datasource;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.everis.ehcos.multitenantsupport.datasource.BasicDbcpDataSourceFactory;
import com.everis.ehcos.multitenantsupport.datasource.IDataSourceFactory;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Factory de DataSources poolables de clase
 * org.apache.commons.dbcp2.BasicDataSource
 *
 */
public class BasicDbcpDataSourceFactory implements IDataSourceFactory, InitializingBean {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(BasicDbcpDataSourceFactory.class);

	// Datasource parameters
	private Boolean defaultAutoCommit;
	private Integer initialSize;
	private Integer maxActive;
	private Integer maxIdle;
	private Long maxWait;
	private Integer minIdle;
	private String validationQuery;
	private Boolean altUser;
	

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public DataSource buildDataSource(IDataSourceConfiguration dataSourceConfiguration) {

		// Log
		logger.info("Construyendo DataSource org.apache.commons.dbcp2.BasicDataSource con parametros: " + dataSourceConfiguration);

		// Creacion DataSource poolable de clase
		// org.apache.commons.dbcp.BasicDatasource
		// La misma que crea por defecto Tomcat
		BasicDataSource dataSource = new BasicDataSource();

		// Configuracion comun para DataSource
		dataSource.setDriverClassName(dataSourceConfiguration.getDriverName());
		dataSource.setUrl(dataSourceConfiguration.getUrl());
		
		// Use user or alternative db user and password
		if (getAltUser() != null && getAltUser() == true) {
			dataSource.setUsername(dataSourceConfiguration.getAltUserName());
			dataSource.setPassword(dataSourceConfiguration.getAltPassword());
		}
		else {
			dataSource.setUsername(dataSourceConfiguration.getUserName());
			dataSource.setPassword(dataSourceConfiguration.getPassword());
		}

		// Default autocommit
		if (getDefaultAutoCommit() != null) {
			dataSource.setDefaultAutoCommit(defaultAutoCommit);
		}
		// Initial size
		if (getInitialSize() != null) {
			dataSource.setInitialSize(initialSize);
		}
		// Max active
		if (getMaxActive() != null) {
			dataSource.setMaxTotal(maxActive);
		}
		// Max idle
		if (getMaxIdle() != null) {
			dataSource.setMaxIdle(maxIdle);
		}
		// Max wait
		if (getMaxWait() != null) {
			dataSource.setMaxWaitMillis(maxWait);
		}
		// Min idle
		if (getMinIdle() != null) {
			dataSource.setMinIdle(minIdle);
		}
		// Validation query
		if (getValidationQuery() != null) {
			dataSource.setValidationQuery(validationQuery);
		}

		// return
		return dataSource;
	}

	public Boolean getDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	public void setDefaultAutoCommit(Boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Long maxWait) {
		this.maxWait = maxWait;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
	
	public Boolean getAltUser() {
		return altUser;
	}

	public void setAltUser(Boolean altUser) {
		this.altUser = altUser;
	}

}

package com.multitenantsupport.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.everis.ehcos.multitenantsupport.datasource.BitronixPoolingDatasourceFactory;
import com.everis.ehcos.multitenantsupport.datasource.IDataSourceFactory;
import com.multitenantsupport.MultitenantContextProvider;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

import bitronix.tm.resource.jdbc.PoolingDataSource;

/**
 * 
 * Factory de DataSources poolables y transaccionales de clase
 * bitronix.tm.resource.jdbc.PoolingDataSource
 *
 */
public class BitronixPoolingDatasourceFactory implements IDataSourceFactory, InitializingBean {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(BitronixPoolingDatasourceFactory.class);

	// Datasource parameters
	private Boolean localAutoCommit;
	private Boolean allowLocalTransactions;
	private String className;
	private Integer maxPoolSize;
	private Integer minPoolSize;
	private Integer maxIdle;
	private Boolean altUser;

	// Nombres de propiedades de conexion para el driver con nombre className
	private String driverClassUrlPropertyName;
	private String driverClassUserPropertyName;
	private String driverClassPasswordPropertyName;
	private String driverClassClassNamePropertyName;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.className, "Driver className must be specified");
		Assert.notNull(this.driverClassUrlPropertyName, "Driver class URL property name must be specified");
		Assert.notNull(this.driverClassUserPropertyName, "Driver class user property name must be specified");
		Assert.notNull(this.driverClassPasswordPropertyName, "Driver class password property name must be specified");
		Assert.notNull(this.driverClassClassNamePropertyName, "Driver class classname property name must be specified");
	}

	@Override
	public DataSource buildDataSource(IDataSourceConfiguration dataSourceConfiguration) {
		
		// Log
		logger.info("Construyendo DataSource bitronix.tm.resource.jdbc.PoolingDataSource con parametros: " + dataSourceConfiguration);
		
		// Creacion DataSource poolable de clase
		// org.apache.commons.dbcp.BasicDatasource
		// La misma que crea por defecto Tomcat
		PoolingDataSource dataSource = new PoolingDataSource();

		// Configuracion comun para DataSource
		dataSource.setClassName(className);

		Properties driverProperties = new Properties();
		driverProperties.setProperty(driverClassUrlPropertyName, dataSourceConfiguration.getUrl());
		driverProperties.setProperty(driverClassClassNamePropertyName, dataSourceConfiguration.getDriverName());
		
		// Use user or alternative db user and password
		if (getAltUser() != null) {
			if(getAltUser() == false) {
				driverProperties.setProperty(driverClassUserPropertyName, dataSourceConfiguration.getUserName());
				driverProperties.setProperty(driverClassPasswordPropertyName, dataSourceConfiguration.getPassword());
			}
			else {
				driverProperties.setProperty(driverClassUserPropertyName, dataSourceConfiguration.getAltUserName());
				driverProperties.setProperty(driverClassPasswordPropertyName, dataSourceConfiguration.getAltPassword());
			}
		}

		dataSource.setDriverProperties(driverProperties);

		// Default autocommit
		if (getLocalAutoCommit() != null) {
			dataSource.setLocalAutoCommit(localAutoCommit.toString());
		}
		// Driver classname
		if (getAllowLocalTransactions() != null) {
			dataSource.setAllowLocalTransactions(allowLocalTransactions);
		}
		// Initial size
		if (getClassName() != null) {
			dataSource.setClassName(className);
		}
		
		// Se proporciona un nombre por tenant al DataSource creado
		MultitenantContextProvider mcp = new MultitenantContextProvider();	
		dataSource.setUniqueName(mcp.getCurrentTenantId());
		
		// Max idle
		if (getMaxPoolSize() != null) {
			dataSource.setMaxPoolSize(maxPoolSize);
		}
		// Max wait
		if (getMinPoolSize() != null) {
			dataSource.setMinPoolSize(minPoolSize);
		}
		// Min idle
		if (getMaxIdle() != null) {
			dataSource.setMaxIdleTime(maxIdle);
		}
		
		// Inicializacion
		dataSource.init();

		// return
		return dataSource;
	}

	public Boolean getLocalAutoCommit() {
		return localAutoCommit;
	}

	public void setLocalAutoCommit(Boolean localAutoCommit) {
		this.localAutoCommit = localAutoCommit;
	}

	public Boolean getAllowLocalTransactions() {
		return allowLocalTransactions;
	}

	public void setAllowLocalTransactions(Boolean allowLocalTransactions) {
		this.allowLocalTransactions = allowLocalTransactions;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public Integer getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(Integer minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public String getDriverClassUrlPropertyName() {
		return driverClassUrlPropertyName;
	}

	public void setDriverClassUrlPropertyName(String driverClassUrlPropertyName) {
		this.driverClassUrlPropertyName = driverClassUrlPropertyName;
	}

	public String getDriverClassUserPropertyName() {
		return driverClassUserPropertyName;
	}

	public void setDriverClassUserPropertyName(String driverClassUserPropertyName) {
		this.driverClassUserPropertyName = driverClassUserPropertyName;
	}

	public String getDriverClassPasswordPropertyName() {
		return driverClassPasswordPropertyName;
	}

	public void setDriverClassPasswordPropertyName(String driverClassPasswordPropertyName) {
		this.driverClassPasswordPropertyName = driverClassPasswordPropertyName;
	}
	
	public String getDriverClassClassNamePropertyName() {
		return driverClassClassNamePropertyName;
	}

	public void setDriverClassClassNamePropertyName(String driverClassClassNamePropertyNamee) {
		this.driverClassClassNamePropertyName = driverClassClassNamePropertyNamee;
	}
	
	public Boolean getAltUser() {
		return altUser;
	}

	public void setAltUser(Boolean altUser) {
		this.altUser = altUser;
	}


}

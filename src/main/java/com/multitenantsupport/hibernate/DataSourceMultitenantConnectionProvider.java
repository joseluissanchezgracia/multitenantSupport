package com.multitenantsupport.hibernate;

import javax.sql.DataSource;

import org.hibernate.service.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.everis.ehcos.multitenantsupport.hibernate.DataSourceMultitenantConnectionProvider;
import com.multitenantsupport.MultitenantContextProvider;
import com.multitenantsupport.datasource.IDataSourceProvider;

/**
 * 
 * Esta clase es utilizada por Hibernate en su mecanismo multitenant para
 * proveer DataSources en cada conexion a base de datos
 *
 */
public class DataSourceMultitenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
		implements InitializingBean {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(DataSourceMultitenantConnectionProvider.class);

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = -4728175813050219647L;

	/**
	 * DataSourceProvider
	 */
	IDataSourceProvider dataSourceProvider;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSourceProvider, "DataSourceProvider must be specified");
	}

	/**
	 * Devuelve el DataSource por defecto
	 */
	@Override
	protected DataSource selectAnyDataSource() {

		// Log
		logger.debug("Obteniendo DataSource por defecto");

		// return
		return dataSourceProvider.getDefaultDataSource();
	}

	/**
	 * Devuelve el DataSource asociado a un tenant
	 */
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		DataSource dataSource = null;

		// Log
		logger.debug("Obteniendo DataSource asociada al tenant Id: " + tenantIdentifier);
		try {

			// Si no esta activado multitenancy devolvemos directamente la
			// dataSource por defecto
			if (MultitenantContextProvider.isMultitenantEnabled() == false) {
				dataSource = selectAnyDataSource();
			} else {

				// Se obtiene en el provider el datasource asociado al tenant
				dataSource = dataSourceProvider.getTenantDataSource(tenantIdentifier);
			}

		} catch (Exception e) {
			// Log
			logger.error("Error al obtener DataSource asociada al tenant Id: " + tenantIdentifier, e);
		}
		
		// Log
		logger.debug("Obtenida DataSource asociada al tenant Id: " + tenantIdentifier + " -> " + dataSource);

		// return
		return dataSource;
	}

	public IDataSourceProvider getDataSourceProvider() {
		return dataSourceProvider;
	}

	public void setDataSourceProvider(IDataSourceProvider dataSourceProvider) {
		this.dataSourceProvider = dataSourceProvider;
	}

}

package com.multitenantsupport.datasource;

import javax.sql.DataSource;

import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Factory para la creacion de DataSources
 *
 */
public interface IDataSourceFactory {

	/**
	 * Construye un nuevo dataSource en base a parametros de configuracion
	 * 
	 * @param dataSourceConfiguration Parametros de configuracion para la creacion de un DataSource
	 * @return
	 */
	public DataSource buildDataSource(IDataSourceConfiguration dataSourceConfiguration);
}

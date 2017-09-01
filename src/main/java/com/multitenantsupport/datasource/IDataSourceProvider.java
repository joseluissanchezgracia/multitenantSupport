package com.multitenantsupport.datasource;

import javax.sql.DataSource;

/**
 * 
 * Gestiona y provee DataSources en un contexto multitenant
 *
 */
public interface IDataSourceProvider {

	/**
	 * Devuelve el DataSource por defecto
	 */
	public DataSource getDefaultDataSource();
	
	/**
	 * Devuelve el DataSource asociado a un tenant Id
	 * 
	 * @param tenantIdentifier Identificador del tenant
	 * @return
	 * @throws Exception
	 */
	public DataSource getTenantDataSource(String tenantIdentifier) throws Exception;
}

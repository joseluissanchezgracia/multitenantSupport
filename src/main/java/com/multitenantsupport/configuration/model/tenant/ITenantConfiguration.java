package com.multitenantsupport.configuration.model.tenant;

import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Interface que contiene la configuracion basica generica de un tenant
 *
 */
public interface ITenantConfiguration {
	
	/**
	 * 
	 * @return Tenant Identifier
	 */
	public String getTenantIdentifier();
	
	/**
	 * 
	 * @param tenantIdentifier Tenant Identifier
	 */
	public void setTenantIdentifier(String tenantIdentifier);
	
	/**
	 * 
	 * @return Datasource configuration
	 */
	public IDataSourceConfiguration getTenantDataSourceConfiguration();
	
	/**
	 * 
	 * @param dataSourceConfiguration Datasource configuration
	 */
	public void setTenantDataSourceConfiguration(IDataSourceConfiguration dataSourceConfiguration);
}

package com.multitenantsupport.configuration.model.tenant;

import com.everis.ehcos.multitenantsupport.configuration.model.tenant.ITenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Implementacion por defecto de configuracion generica de tenant
 *
 */
public class TenantConfiguration implements ITenantConfiguration {

	// Identificador del tenant
	private String tenantIdentifier;

	// Configuracion del datasource del tenant
	private IDataSourceConfiguration dataSourceConfiguration;

	
	public String getTenantIdentifier() {
		return tenantIdentifier;
	}

	public void setTenantIdentifier(String tenantIdentifier) {
		this.tenantIdentifier = tenantIdentifier;
	}

	public IDataSourceConfiguration getTenantDataSourceConfiguration() {
		return dataSourceConfiguration;
	}

	public void setTenantDataSourceConfiguration(IDataSourceConfiguration dataSourceConfiguration) {
		this.dataSourceConfiguration = dataSourceConfiguration;
	}

	@Override
	public String toString() {
		
		StringBuilder stBuilder = new StringBuilder();
		stBuilder.append("TenantConfiguration: ");
		
		stBuilder.append("[Tenant Identifier: ");
		stBuilder.append(tenantIdentifier);
		stBuilder.append("], ");
		
		stBuilder.append("[DataSource Configuration: ");
		stBuilder.append(dataSourceConfiguration.toString());
		stBuilder.append("]");
		
		return stBuilder.toString();
	}


	
}

package com.multitenantsupport.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.springframework.beans.factory.FactoryBean;

import com.everis.ehcos.multitenantsupport.configuration.ITenantConfigurationProvider;
import com.google.gson.Gson;
import com.multitenantsupport.configuration.gson.TenantConfigurationGsonFactory;
import com.multitenantsupport.configuration.model.tenant.ITenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.TenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Implementacion por defecto de proveedor de configuraciones de tenants
 *
 */
public class TenantConfigurationProvider
		implements ITenantConfigurationProvider<ITenantConfiguration> {

	// Factory de objetos gson para TenantConfigurations
	FactoryBean<Gson> gsonFactory = new TenantConfigurationGsonFactory();

	// Proveedor de configuracion Json
	ITenantConfigurationProvider<String> jsonConfigurationProvider;

	@Override
	public ITenantConfiguration getTenantConfiguration(String tenantIdentifier) throws Exception {

		// Obtencion de la configuracion como String con formato Json
		String jsonConfiguration = jsonConfigurationProvider.getTenantConfiguration(tenantIdentifier);

		// Convertimos a objeto configuracion
		return gsonFactory.getObject().fromJson(jsonConfiguration, TenantConfiguration.class);

	}

	@Override
	public List<ITenantConfiguration> getTenantsConfigurations() throws Exception {

		// Obtencion de la configuracion como String con formato Json
		List<String> jsonConfigurations = jsonConfigurationProvider.getTenantsConfigurations();

		// Convertimos a objeto configuracion
		List<ITenantConfiguration> tenantConfigurations = new ArrayList<ITenantConfiguration>();
		for (String jsonConfiguration : jsonConfigurations) {
			tenantConfigurations.add(gsonFactory.getObject().fromJson(jsonConfiguration, TenantConfiguration.class));
		}

		// return
		return tenantConfigurations;
	}

	public ITenantConfigurationProvider<String> getJsonConfigurationProvider() {
		return jsonConfigurationProvider;
	}

	public void setJsonConfigurationProvider(ITenantConfigurationProvider<String> jsonConfigurationProvider) {
		this.jsonConfigurationProvider = jsonConfigurationProvider;
	}

	@Override
	public void addObserver(Observer o) {
		this.jsonConfigurationProvider.addObserver(o);
	}

	@Override
	public void deleteObserver(Observer o) {
		this.jsonConfigurationProvider.deleteObserver(o);
	}

}

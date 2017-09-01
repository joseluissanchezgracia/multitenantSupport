package com.multitenantsupport.configuration.events;

import org.springframework.beans.factory.FactoryBean;

import com.everis.ehcos.multitenantsupport.configuration.events.ITenantConfigurationEvent;
import com.everis.ehcos.multitenantsupport.configuration.events.ITenantConfigurationEventFactory;
import com.everis.ehcos.multitenantsupport.configuration.events.TenantConfigurationDeletedEvent;
import com.everis.ehcos.multitenantsupport.configuration.events.TenantConfigurationModifiedEvent;
import com.google.gson.Gson;
import com.multitenantsupport.configuration.gson.TenantConfigurationGsonFactory;
import com.multitenantsupport.configuration.model.tenant.TenantConfiguration;

/**
 * 
 * Factory de eventos de clase TenantConfiguration
 *
 */
public class TenantConfigurationEventFactory implements ITenantConfigurationEventFactory {

	// Factory de objetos gson para TenantConfigurations
	private FactoryBean<Gson> gsonFactory = new TenantConfigurationGsonFactory();

	/**
	 * Construye evento para informar de borrado de configuracion de tenant
	 */
	@Override
	public ITenantConfigurationEvent buildDeletedConfigurationEvent(String tenantIdentifier) {

		TenantConfigurationDeletedEvent event = new TenantConfigurationDeletedEvent();
		event.setTenantIdentifier(tenantIdentifier);

		return event;
	}

	/**
	 * Construye evento para informar de modificacion o anyadido de configuracion de tenant
	 */
	@Override
	public ITenantConfigurationEvent buildModifiedConfigurationEvent(String tenantIdentifier,
			String tenantConfiguration) throws Exception {

		TenantConfigurationModifiedEvent event = new TenantConfigurationModifiedEvent();
		event.setTenantIdentifier(tenantIdentifier);
		event.setTenantConfiguration(gsonFactory.getObject().fromJson(tenantConfiguration, TenantConfiguration.class));

		return event;
	}

}

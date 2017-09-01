package com.multitenantsupport.configuration.events;

import com.everis.ehcos.multitenantsupport.configuration.events.AbstractTenantConfigurationEvent;
import com.multitenantsupport.configuration.model.tenant.ITenantConfiguration;

/**
 * 
 * Clase evento que incluye informacion sobre la configuracion que lo dispara
 *
 */
public abstract class AbstractTenantConfigurationWithSourceEvent extends AbstractTenantConfigurationEvent {

	ITenantConfiguration tenantConfiguration;
	
	public ITenantConfiguration getTenantConfiguration() {
		return this.tenantConfiguration;
	}
	
	public void setTenantConfiguration(ITenantConfiguration tenantConfiguration) {
		this.tenantConfiguration = tenantConfiguration;
	}
}

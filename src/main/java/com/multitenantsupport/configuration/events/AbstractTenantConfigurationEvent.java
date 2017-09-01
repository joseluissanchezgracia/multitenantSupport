package com.multitenantsupport.configuration.events;

import com.everis.ehcos.multitenantsupport.configuration.events.ITenantConfigurationEvent;

/**
 * 
 * Clase evento que incluye informacion sobre el tenant cuya configuracion ha disparado dicho evento
 *
 */
public abstract class AbstractTenantConfigurationEvent implements ITenantConfigurationEvent {

	protected String tenantIdentifier;

	@Override
	public String getTenantIdentifier() {
		return tenantIdentifier;
	}

	@Override
	public void setTenantIdentifier(String tenantIdentifier) {
		this.tenantIdentifier = tenantIdentifier;
	}
	
}

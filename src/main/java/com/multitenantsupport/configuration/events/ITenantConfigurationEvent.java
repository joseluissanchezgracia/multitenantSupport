package com.multitenantsupport.configuration.events;

/**
 * 
 * Interfaz base para eventos de configuracion de tenant
 *
 */
public interface ITenantConfigurationEvent {

	public String getTenantIdentifier();
	
	public void setTenantIdentifier(String tenantIdentifier);
}

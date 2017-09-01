package com.multitenantsupport.configuration.events;

import com.everis.ehcos.multitenantsupport.configuration.events.ITenantConfigurationEvent;

/**
 * 
 * Interfaz de factory para construccion de eventos disparados desde un Configuration Provider
 *
 */
public interface ITenantConfigurationEventFactory {

	/**
	 * Construye un evento por borrado de configuracion de un tenant
	 * 
	 * @param tenantIdentifier
	 * @return
	 */
	public ITenantConfigurationEvent buildDeletedConfigurationEvent(String tenantIdentifier);
	
	/**
	 * Construye un evento por modificado o anyadido de configuracion de un tenant
	 * 
	 * @param tenantIdentifier
	 * @param tenantConfiguration
	 * @return
	 * @throws Exception
	 */
	public ITenantConfigurationEvent buildModifiedConfigurationEvent(String tenantIdentifier, String tenantConfiguration) throws Exception;
}

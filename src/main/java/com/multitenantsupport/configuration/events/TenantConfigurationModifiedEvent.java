package com.multitenantsupport.configuration.events;

import com.everis.ehcos.multitenantsupport.configuration.events.AbstractTenantConfigurationWithSourceEvent;

/**
 * 
 * Evento disparado por configuracion de tenant modificada o añadida en el proveedor de configuracion
 *
 */
public class TenantConfigurationModifiedEvent extends AbstractTenantConfigurationWithSourceEvent {

	@Override
	public String toString() {
		StringBuilder stBuilder = new StringBuilder();
		stBuilder.append("TenantConfigurationModifiedEvent: ");

		stBuilder.append("[Tenant identifier: ");
		stBuilder.append(tenantIdentifier);
		stBuilder.append("], ");
		
		stBuilder.append("[New tenant configuration: ");
		stBuilder.append(getTenantConfiguration());
		stBuilder.append("]");

		return stBuilder.toString();
	}
	
}

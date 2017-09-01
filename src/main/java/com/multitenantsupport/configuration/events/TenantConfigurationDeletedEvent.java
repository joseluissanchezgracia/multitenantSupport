package com.multitenantsupport.configuration.events;

import com.everis.ehcos.multitenantsupport.configuration.events.AbstractTenantConfigurationEvent;

/**
 * 
 * Evento disparado por configuracion de tenant borrada en el proveedor de configuracion
 *
 */
public class TenantConfigurationDeletedEvent extends AbstractTenantConfigurationEvent {

	@Override
	public String toString() {
		StringBuilder stBuilder = new StringBuilder();
		stBuilder.append("TenantConfigurationDeletedEvent: ");

		stBuilder.append("[Tenant identifier: ");
		stBuilder.append(tenantIdentifier);
		stBuilder.append("]");

		return stBuilder.toString();
	}

}

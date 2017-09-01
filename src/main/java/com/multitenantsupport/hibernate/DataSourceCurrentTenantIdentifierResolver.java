package com.multitenantsupport.hibernate;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import com.multitenantsupport.MultitenantContextProvider;


/**
 * 
 * Clase empleada por el SessionFactory de Hibernate para resolver dinamicamente
 * lo que la aplicacion considera el tenantID actual
 *
 */
public class DataSourceCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	/**
	 * Devuelve el identificador de tenant para la peticion que ha desencadenado
	 * el acceso a base de datos, cadena vacia si no ha lugar
	 */
	@Override
	public String resolveCurrentTenantIdentifier() {
		return new MultitenantContextProvider().getCurrentTenantId();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}

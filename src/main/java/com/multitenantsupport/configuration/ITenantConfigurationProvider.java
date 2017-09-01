package com.multitenantsupport.configuration;


import java.util.List;
import java.util.Observer;

/**
 * 
 * Interfaz generico de proveedor de configuraciones de tenant
 *
 * @param <T>
 * @param <D>
 */
public interface ITenantConfigurationProvider<T> {

	/**
	 * Devuelve configuracion de tenant
	 * 
	 * @param tenantIdentifier Identificador del tenant
	 * @return
	 * @throws Exception
	 */
	public T getTenantConfiguration(String tenantIdentifier) throws Exception;
	
	/**
	 * Devuelve la configuracion de todos los tenants
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<T> getTenantsConfigurations() throws Exception;
	
	/**
	 * Registra un observer para enviarle eventos por cambios en configuraciones
	 * 
	 * @param o
	 */
	public void addObserver(Observer o );
	
	/**
	 * Desregistra un observer de eventos por cambios en configuraciones
	 * 
	 * @param o
	 */
	public void deleteObserver(Observer o );
	
}

package com.multitenantsupport.configuration.gson;

/**
 * 
 * Interfaz con constantes de los campos de configuracion Json de un tenant
 *
 */
public interface TenantConfigurationGsonConstants {

	// Constantes de campos de configuracion
	public final static String TENANT_CONFIGURATION_FIELD_TENANT = "tenant";
	public final static String TENANT_CONFIGURATION_FIELD_DATASOURCE_CONFIGURATION = "datasourceconfiguration";

	// Constantes de campos de configuracion
	public final static String DATASOURCE_CONFIGURATION_FIELD_DRIVERNAME = "driver";
	public final static String DATASOURCE_CONFIGURATION_FIELD_USERNAME = "username";
	public final static String DATASOURCE_CONFIGURATION_FIELD_PASSWORD = "password";
	public final static String DATASOURCE_CONFIGURATION_FIELD_URL = "url";
	public final static String DATASOURCE_CONFIGURATION_FIELD_ALT_USERNAME = "altusername";
	public final static String DATASOURCE_CONFIGURATION_FIELD_ALT_PASSWORD = "altpassword";
}

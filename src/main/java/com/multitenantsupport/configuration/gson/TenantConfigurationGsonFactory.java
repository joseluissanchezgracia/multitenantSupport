package com.multitenantsupport.configuration.gson;

import org.springframework.beans.factory.FactoryBean;

import com.everis.ehcos.multitenantsupport.configuration.gson.TenantConfigurationGsonAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.multitenantsupport.configuration.model.tenant.TenantConfiguration;

/**
 * 
 * Factory de objetos Gson para conversion toJson y fromJson de objetos TenantConfiguration
 *
 */
public class TenantConfigurationGsonFactory implements FactoryBean<Gson> {

	// Builder de Gsons
	private GsonBuilder tenantConfigurationGsonBuilder;
	
	// Constructor
	public TenantConfigurationGsonFactory() {
		super();
		
		// Se inicializa el GsonBuilder con el TypeAdapter apropiado para
		tenantConfigurationGsonBuilder = new GsonBuilder();
		tenantConfigurationGsonBuilder.registerTypeAdapter(TenantConfiguration.class, 
				new TenantConfigurationGsonAdapter());
		tenantConfigurationGsonBuilder.setPrettyPrinting();
	}

	/**
	 * Devuelve un Gson
	 */
	@Override
	public Gson getObject() throws Exception {
		return tenantConfigurationGsonBuilder.create();
	}

	@Override
	public Class<Gson> getObjectType() {
		return Gson.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}

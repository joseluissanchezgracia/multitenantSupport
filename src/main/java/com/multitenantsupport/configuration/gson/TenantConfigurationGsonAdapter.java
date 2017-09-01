package com.multitenantsupport.configuration.gson;

import java.io.IOException;

import com.everis.ehcos.multitenantsupport.configuration.gson.TenantConfigurationGsonConstants;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.multitenantsupport.configuration.model.tenant.ITenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.TenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.datasource.DataSourceConfiguration;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Clase adaptador para conversion Json-objeto de TenantConfiguration en combinacion con un GsonBuilder
 *
 */
public class TenantConfigurationGsonAdapter extends TypeAdapter<ITenantConfiguration>
		implements TenantConfigurationGsonConstants {

	@Override
	public ITenantConfiguration read(final JsonReader in) throws IOException {

		// Tenant configuration a devolver
		final TenantConfiguration tenantConfiguration = new TenantConfiguration();

		// Se parsea el Json y se genera objeto
		in.beginObject();
		while (in.hasNext()) {
			String fieldName = in.nextName();
			// tenant
			if (fieldName.equalsIgnoreCase(TENANT_CONFIGURATION_FIELD_TENANT)) {
				tenantConfiguration.setTenantIdentifier(in.nextString());
			}
			// datasourceconfiguration
			else if (fieldName.equalsIgnoreCase(TENANT_CONFIGURATION_FIELD_DATASOURCE_CONFIGURATION)) {
				tenantConfiguration.setTenantDataSourceConfiguration(readDataSourceConfiguration(in));
			}
			// Error por campo de configuracion no contemplado
			else {
				throw new IllegalArgumentException("Campo de configuracion de tenant desconocido: " + fieldName);
			}
		}
		in.endObject();

		// Chequeo de campos no nulos
		if (tenantConfiguration.getTenantIdentifier() == null
				|| tenantConfiguration.getTenantDataSourceConfiguration() == null) {
			throw new IllegalStateException("Configuracion de tenant incompleta: " + in.toString());
		}

		return tenantConfiguration;

	}

	/**
	 * Lee DataSource configuration
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public IDataSourceConfiguration readDataSourceConfiguration(JsonReader in) throws IOException {
		// DataSource configuration a devolver
		final DataSourceConfiguration dataSourceConfiguration = new DataSourceConfiguration();

		// Se parsea el Json y se genera objeto
		in.beginObject();
		while (in.hasNext()) {
			String fieldName = in.nextName();
			// driver
			if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_DRIVERNAME)) {
				dataSourceConfiguration.setDriverName(in.nextString());
			}
			// username
			else if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_USERNAME)) {
				dataSourceConfiguration.setUserName(in.nextString());
			}
			// password
			else if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_PASSWORD)) {
				dataSourceConfiguration.setPassword(in.nextString());
			}
			// url
			else if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_URL)) {
				dataSourceConfiguration.setUrl(in.nextString());
			}
			// alt username
			else if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_ALT_USERNAME)) {
				dataSourceConfiguration.setAltUserName(in.nextString());
			}
			// alt password
			else if (fieldName.equalsIgnoreCase(DATASOURCE_CONFIGURATION_FIELD_ALT_PASSWORD)) {
				dataSourceConfiguration.setAltPassword(in.nextString());
			}
			// Error por campo de configuracion no contemplado
			else {
				throw new IllegalArgumentException("Campo de configuracion de datasource desconocido: " + fieldName);
			}
		}
		in.endObject();

		// Chequeo de campos no nulos
		if (dataSourceConfiguration.getDriverName() == null || dataSourceConfiguration.getPassword() == null
				|| dataSourceConfiguration.getUserName() == null || dataSourceConfiguration.getUrl() == null) {
			throw new IllegalStateException("Configuracion de datasource incompleta: " + in.toString());
		}

		return dataSourceConfiguration;
	}

	/**
	 * Escribe Datasource configuration
	 */
	@Override
	public void write(final JsonWriter out, final ITenantConfiguration tenantConfiguration) throws IOException {

		// Adaptador Json
		/*Gson dataSourceGson = dataSourceGsonBuilder.create();

		out.beginObject();
		out.name(TENANT_CONFIGURATION_FIELD_TENANT).value(tenantConfiguration.getTenantIdentifier());
		out.name(TENANT_CONFIGURATION_FIELD_DATASOURCE_CONFIGURATION)
				.value(dataSourceGson.toJson(tenantConfiguration.getTenantDataSourceConfiguration()));
		out.endObject();*/

		/*
		 * out.beginObject();
		 * out.name(DATASOURCE_CONFIGURATION_FIELD_DRIVERNAME).value(
		 * dataSourceConfiguration.getDriverName());
		 * out.name(DATASOURCE_CONFIGURATION_FIELD_USERNAME).value(
		 * dataSourceConfiguration.getUserName());
		 * out.name(DATASOURCE_CONFIGURATION_FIELD_PASSWORD).value(
		 * dataSourceConfiguration.getPassword());
		 * out.name(DATASOURCE_CONFIGURATION_FIELD_URL).value(
		 * dataSourceConfiguration.getUrl());
		 */

		out.endObject();
	}

}

package com.multitenantsupport.datasource;

import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.everis.ehcos.multitenantsupport.datasource.DataSourceProvider;
import com.everis.ehcos.multitenantsupport.datasource.IDataSourceFactory;
import com.everis.ehcos.multitenantsupport.datasource.IDataSourceProvider;
import com.multitenantsupport.configuration.ITenantConfigurationProvider;
import com.multitenantsupport.configuration.events.TenantConfigurationDeletedEvent;
import com.multitenantsupport.configuration.events.TenantConfigurationModifiedEvent;
import com.multitenantsupport.configuration.model.tenant.ITenantConfiguration;
import com.multitenantsupport.configuration.model.tenant.datasource.IDataSourceConfiguration;

/**
 * 
 * Gestiona y provee DataSources en un contexto multitenant
 *
 */
public class DataSourceProvider implements IDataSourceProvider, InitializingBean, Observer {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(DataSourceProvider.class);

	/**
	 * Factory de DataSources
	 */
	private IDataSourceFactory dataSourceFactory;

	/**
	 * Objeto a partir del cual construir el DataSource por defecto. Puede
	 * inyectarse directamente un DataSource por configuracion o un FactoryBean
	 * para generacion de DataSources El DataSource por defecto
	 */
	private Object defaultDataSourceRef;

	/**
	 * DataSource por defecto, que es el DataSource gestionado por el contenedor
	 * JEE
	 */
	private DataSource defaultDataSource;

	/**
	 * Proveedor de configuraciones de tenant
	 */
	private ITenantConfigurationProvider<ITenantConfiguration> tenantConfigurationProvider;

	/**
	 * Repositorio de pares tenantId:DataSource Debe accederse con synchronized
	 */
	private Hashtable<String, DataSource> dataSourcesRepository = new Hashtable<String, DataSource>();

	/**
	 * Devuelve el DataSource por defecto
	 */
	@Override
	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	/**
	 * Chequea que se hayan inyectado las propiedades obligatorias Inicializa el
	 * bean
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(dataSourceFactory, "DataSourceFactory must be specified");
		Assert.notNull(defaultDataSourceRef, "DefaultDataSourceRef must be specified");
		// Assert.notNull(tenantConfigurationProvider,
		// "TenantConfigurationProvider must be specified");

		// Se añade este bean como observador del proveedor de configuracion
		// para eventos sobre configuraciones de DataSource
		if (tenantConfigurationProvider != null) {
			tenantConfigurationProvider.addObserver(this);
		}

		// Se inicializan los dataSources que pueda haber configurados en el
		// proveedor de configuracion
		/*
		 * List<ITenantConfiguration> tenantConfigurations =
		 * tenantConfigurationProvider.getTenantsConfigurations();
		 * 
		 * for (ITenantConfiguration tenantConfiguration : tenantConfigurations)
		 * {
		 * 
		 * IDataSourceConfiguration dataSourceConfiguration =
		 * tenantConfiguration.getTenantDataSourceConfiguration(); String
		 * tenantId = tenantConfiguration.getTenantIdentifier();
		 * 
		 * // Cada DataSource creada se registra en el repositorio
		 * registerNewOrModifiedDataSource(tenantId, dataSourceConfiguration); }
		 */
	}

	public IDataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	public void setDataSourceFactory(IDataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}

	public Object getDefaultDataSourceRef() {
		return defaultDataSourceRef;
	}

	public ITenantConfigurationProvider<ITenantConfiguration> getTenantConfigurationProvider() {
		return tenantConfigurationProvider;
	}

	public void setTenantConfigurationProvider(
			ITenantConfigurationProvider<ITenantConfiguration> tenantConfigurationProvider) {
		this.tenantConfigurationProvider = tenantConfigurationProvider;
	}

	/**
	 * Posible objetos a inyectar: -FactoryBean -DataSource
	 * 
	 * @param defaultDataSourceRef
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setDefaultDataSourceRef(Object defaultDataSourceRef) throws Exception {
		this.defaultDataSourceRef = defaultDataSourceRef;

		// Por JNDI obtenemos la dataSource configurada por defecto
		if (defaultDataSourceRef instanceof FactoryBean<?>) {
			defaultDataSource = ((FactoryBean<DataSource>) defaultDataSourceRef).getObject();
		}
		// Por JDBC obtenemos la dataSource configurada por defecto
		else if (defaultDataSourceRef instanceof DataSource) {
			defaultDataSource = (DataSource) defaultDataSourceRef;
		}
		// La clase del bean no es adecuada
		else {
			throw new BeanInitializationException(
					"El defaultDataSourceRef debe ser un DataSource o un FactoryBean de DataSources");
		}
	}

	/**
	 * Devuelve el DataSource asociado a un tenant Id
	 */
	@Override
	public DataSource getTenantDataSource(String tenantIdentifier) throws Exception {

		// log
		logger.debug("Obteniendo DataSource asociado al tenant: " + tenantIdentifier);

		// DataSource
		DataSource dataSource;

		// Si el tenant identifier es nulo o vacio entonces devolvemos el datasource por defecto
		// Posiblemente estamos con el modo multitenant no activado
		if (tenantIdentifier == null || tenantIdentifier.isEmpty()) {
			
			// log
			logger.debug("Obteniendo DataSource por derfecto para tenant id: " + tenantIdentifier);
			
			dataSource = getDefaultDataSource();
			
		// Con tenant identifier valido, obtenemos el dataSource del repositorio	
		} else {
			// Bloque sincronizado para acceso al repositorio de DataSources
			synchronized (dataSourcesRepository) {
				
				// log
				logger.debug("Obteniendo DataSource de repositorio para tenant id: " + tenantIdentifier);
				
				dataSource = dataSourcesRepository.get(tenantIdentifier);

				// Se intenta obtener el DataSource de un tenant y si no existe se crea
				if (dataSource == null) {

					// Log
					logger.info("DataSource nulo asociado al tenant: " + tenantIdentifier);

					// Se obtiene la configuracion de DataSource del tenant
					ITenantConfiguration tenantConfiguration = tenantConfigurationProvider
							.getTenantConfiguration(tenantIdentifier);

					
					IDataSourceConfiguration dataSourceConfiguration = tenantConfiguration
							.getTenantDataSourceConfiguration();

					// Log
					logger.info("Obtenida DataSource Configuration asociada al tenant: " + tenantIdentifier + " -> "
							+ dataSourceConfiguration);

					dataSource = dataSourceFactory.buildDataSource(dataSourceConfiguration);

					// Log
					logger.info("Creada DataSource asociada al tenant: " + tenantIdentifier);

					// Anyadir el datasource creado al repositorio
					dataSourcesRepository.put(tenantIdentifier, dataSource);

					// Log
					logger.info("Registrada DataSource asociada al tenant: " + tenantIdentifier);
				}
			}
		}

		// return
		return dataSource;
	}

	/**
	 * Añade al registro una nueva asociacion tenant:datasource creando una
	 * nueva DataSource
	 * 
	 * @param tenantIdentifier
	 * @param dataSourceConfiguration
	 */
	private void registerNewOrModifiedDataSource(String tenantIdentifier,
			IDataSourceConfiguration dataSourceConfiguration) {

		// Log
		logger.info("Registrando modificaciones o nueva DataSource asociado al tenant: " + tenantIdentifier + " -> "
				+ dataSourceConfiguration);

		// Synchronized
		synchronized (dataSourcesRepository) {
			
			// Construye el DataSource
			DataSource dataSource = dataSourceFactory.buildDataSource(dataSourceConfiguration);

			// Log
			logger.info("Creada DataSource asociada al tenant: " + tenantIdentifier);

			// Registra el DataSource
			dataSourcesRepository.put(tenantIdentifier, dataSource);
		}
		
		// Log
		logger.info("Registrada DataSource asociada al tenant: " + tenantIdentifier);
	}

	/**
	 * Borrar o desregistra el DataSource de un tenant
	 * 
	 * @param tenantIdentifier
	 */
	private void unregisterDataSource(String tenantIdentifier) {

		// Log
		logger.info("Registrando modificaciones o nueva DataSource asociado al tenant: " + tenantIdentifier);

		// Synchronized
		synchronized (dataSourcesRepository) {
			dataSourcesRepository.remove(tenantIdentifier);
		}
		
		// Log
		logger.info("Desregistrada DataSource asociada al tenant: " + tenantIdentifier);
	}

	/**
	 * Gestion de eventos desde el ConfigurationProvider para modificar los
	 * DataSources en caliente
	 */
	@Override
	public void update(Observable arg0, Object arg1) {

		// Log
		logger.info("Recibido evento desde proveedor de configuracion: " + arg1.toString());

		// Se ha borrado la configuracion de un tenant
		if (arg1 instanceof TenantConfigurationDeletedEvent) {
			TenantConfigurationDeletedEvent event = (TenantConfigurationDeletedEvent) arg1;

			// Desregistramos el posible datasource asociado
			unregisterDataSource(event.getTenantIdentifier());
		}
		// Se ha creado o modificado una configuracion de tenant
		else if (arg1 instanceof TenantConfigurationModifiedEvent) {
			TenantConfigurationModifiedEvent event = (TenantConfigurationModifiedEvent) arg1;

			// Registramos los cambios creando un nuevo DataSource
			registerNewOrModifiedDataSource(event.getTenantIdentifier(),
					event.getTenantConfiguration().getTenantDataSourceConfiguration());
		}
		// Evento desconocido
		else {
			throw new IllegalArgumentException("Evento desconocido");
		}

	}

}

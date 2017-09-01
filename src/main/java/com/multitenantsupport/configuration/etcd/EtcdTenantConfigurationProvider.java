package com.multitenantsupport.configuration.etcd;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.everis.ehcos.multitenantsupport.configuration.etcd.EtcdTenantConfigurationProvider;
import com.multitenantsupport.configuration.ITenantConfigurationProvider;
import com.multitenantsupport.configuration.events.ITenantConfigurationEvent;
import com.multitenantsupport.configuration.events.ITenantConfigurationEventFactory;
import com.multitenantsupport.configuration.gson.TenantConfigurationGsonConstants;

import mousio.client.promises.ResponsePromise;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdKeysResponse.EtcdNode;

/**
 * 
 * Configuration Provider basado en la base de datos de pares clave-valor Etcd
 *
 */
public class EtcdTenantConfigurationProvider extends Observable
		implements ITenantConfigurationProvider<String>, TenantConfigurationGsonConstants {

	// Logger
	private static Logger logger = LoggerFactory.getLogger(EtcdTenantConfigurationProvider.class);

	/**
	 * Dentro de Etcd, directorio comun donde se guardan las configuraciones de
	 * tenants
	 */
	private String tenantsConfigurationPath;

	/**
	 * Cliente Etcd
	 */
	private EtcdClient etcdClient;

	/**
	 * Factory de eventos de configuracion
	 */
	private ITenantConfigurationEventFactory eventFactory;

	/**
	 * Caracter separador de directorios en las claves de Etcd
	 */
	protected final static String ETCD_KEY_PATH_SEPARATOR = "/";

	/**
	 * Segundos por defecto para configurar timeout en las peticiones etcd
	 */
	protected final static long ETCD_CLIENT_SECONDS_TIMEOUT = 5;

	/**
	 * Constructor
	 * 
	 * @param etcdServerUri
	 *            URI de conexion al servidor Etcd
	 * @param tenantsConfigurationPath
	 *            Directorio comun de Etcd donde se guardan las configuraciones
	 *            de tenants. Por defecto "/tenants"
	 * @throws IOException
	 */
	public EtcdTenantConfigurationProvider(String etcdServerUri, String tenantsConfigurationPath) throws IOException {
		super();
		this.tenantsConfigurationPath = tenantsConfigurationPath;

		// Log
		logger.info("Creando EtcdTenantConfigurationProvider con uri '" + etcdServerUri + "' y path '"
				+ tenantsConfigurationPath + "'");

		// Creacion del cliente Etcd
		etcdClient = new EtcdClient(URI.create(etcdServerUri));

		// Activamos el mecanismo de watching
		activateTenantsConfigurationWatching(tenantsConfigurationPath);

	}

	/**
	 * Obtiene de Etcd la configuracion de un tenant
	 */
	@Override
	public String getTenantConfiguration(String tenantIdentifier) throws Exception {

		// Log
		logger.debug("Obtencion de configuracion de tenant: " + tenantIdentifier);

		// Construccion de la key que queremos consultar
		// Por defecto: /tenants/<tenantIdentifier>
		StringBuffer tenantConfigurationKeyBuffer = new StringBuffer();
		tenantConfigurationKeyBuffer.append(ETCD_KEY_PATH_SEPARATOR);
		tenantConfigurationKeyBuffer.append(tenantsConfigurationPath);
		tenantConfigurationKeyBuffer.append(ETCD_KEY_PATH_SEPARATOR);
		tenantConfigurationKeyBuffer.append(tenantIdentifier);

		// Log
		logger.debug("Key para la obtencion de configuracion de tenant: " + tenantConfigurationKeyBuffer.toString());

		EtcdKeysResponse response = etcdClient.get(tenantConfigurationKeyBuffer.toString())
				.timeout(ETCD_CLIENT_SECONDS_TIMEOUT, TimeUnit.SECONDS).send().get();

		// Valor de la configuracion del tenant
		String tenantConfiguration = response.node.value;

		// Log
		logger.debug("Configuracion de tenant obtenida: " + tenantConfiguration);

		// return
		return tenantConfiguration;
	}

	/**
	 * Obtiene de Etcd la configuracion de todos los tenants
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTenantsConfigurations() throws Exception {

		// Log
		logger.debug("Obtencion de configuraciones de tenants");

		// Construccion de la key que queremos consultar
		// Por defecto: /tenants
		StringBuffer tenantsConfigurationKeyBuffer = new StringBuffer();
		tenantsConfigurationKeyBuffer.append(ETCD_KEY_PATH_SEPARATOR);
		tenantsConfigurationKeyBuffer.append(tenantsConfigurationPath);

		// Log
		logger.debug("Key para la obtencion de configuracion de tenants: " + tenantsConfigurationKeyBuffer.toString());

		// Obtenemos la configuracion del tenant
		EtcdKeysResponse response = etcdClient.getDir(tenantsConfigurationKeyBuffer.toString()).send().get();

		ArrayList<String> tenantConfigurations = new ArrayList<String>();

		// Creamos lista de las configuraciones
		for (EtcdNode node : response.node.getNodes()) {

			// Anyadimos configuracion al array de configuraciones de tenants
			tenantConfigurations.add(node.getValue());

			// Log
			logger.debug("Configuracion de tenant obtenida: " + node.getValue());
		}

		// return
		return tenantConfigurations;
	}

	public ITenantConfigurationEventFactory getEventFactory() {
		return eventFactory;
	}

	public void setEventFactory(ITenantConfigurationEventFactory eventFactory) {
		this.eventFactory = eventFactory;
	}

	/**
	 * Metodo para activar la escucha de eventos en cualquier modificacion,
	 * anyadido o borrado de configuracion de tenants
	 * 
	 * @param tenantsConfigurationKey
	 *            Path Etcd donde se guarda la configuracion de tenants
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void activateTenantsConfigurationWatching(final String tenantsConfigurationKey) throws IOException {

		// Se guarda este objeto como final para poder usarlo dentro del
		// listener anonimo que sigue
		final EtcdTenantConfigurationProvider tenantConfigurationProvider = this;

		// Se activa un mecanismo watch para escuchar asincronamente cambios en
		// la configuracion de cualquier tenant asi como borrados o anyadidos de
		// configuraciones
		@SuppressWarnings("rawtypes")
		EtcdResponsePromise promise = etcdClient.get(tenantsConfigurationKey).waitForChange().recursive().send();
		promise.addListener(new ResponsePromise.IsSimplePromiseResponseHandler() {
			@Override
			public void onResponse(ResponsePromise responsePromise) {
				try {
					EtcdKeysResponse response = (EtcdKeysResponse) responsePromise.get();

					logger.info("Activado watch desde Etcd con clave: '" + response.node.key + "' y valor: '"
							+ response.node.value + "'");

					// Obtenemos de la clave Etcd el tenant Id
					String fullTenantConfigurationKey = response.node.key;
					String tenantIdentifier = fullTenantConfigurationKey
							.substring(fullTenantConfigurationKey.lastIndexOf(ETCD_KEY_PATH_SEPARATOR) + 1);

					// Si el tenant identifier es valido
					if (tenantIdentifier.isEmpty() == false) {

						ITenantConfigurationEvent event;
						// Configuracion ha sido borrada
						if (response.node.value == null) {
							event = eventFactory.buildDeletedConfigurationEvent(tenantIdentifier);
						}
						// Configuracion ha sido anyadida o modificada
						else {
							event = eventFactory.buildModifiedConfigurationEvent(tenantIdentifier, response.node.value);
						}

						// Notificamos evento a observadores
						tenantConfigurationProvider.setChanged();
						tenantConfigurationProvider.notifyObservers(event);
					}

				} catch (Exception e) {

					// Log
					logger.error("Error al procesar un evento asincrono por mecanismo de watching en Etcd", e);
				}
				finally {
					// Una vez disparado un evento por watch, hay que rearmar el watch
					try {
						etcdClient.get(tenantsConfigurationKey).waitForChange().recursive().send().addListener(this);
					} catch (IOException e) {
						// Log
						logger.error("Error al rearmar mecanismo de watch de configuracion en Etcd", e);
					}
				}
			}
		});
	}

}

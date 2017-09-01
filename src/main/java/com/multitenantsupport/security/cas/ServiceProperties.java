package com.multitenantsupport.security.cas;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.everis.ehcos.multitenantsupport.security.cas.IServiceProperties;
import com.multitenantsupport.MultitenantContextProvider;

/**
 * Clase de Spring Security 3.0.5 que hemos copiado para poder redirigir las peticiones a CAS con los tenant ID correctos
 * No ha sido posible extenderla porque los metodos son finales. Modificaciones respecto al original:
 * - Implementa IServiceProperties
 * - Metodo getService convierte la URL del service a multitenant si es necesario
 * 
 * The Class ServiceProperties. Copy of
 * org.springframework.security.cas.ServiceProperties version 3.0.5 Used to to
 * dinamically set request URI as Service parameter in order to keep URI as
 * tenant identifier.
 */
public class ServiceProperties implements IServiceProperties, InitializingBean {

	public static final String DEFAULT_CAS_ARTIFACT_PARAMETER = "ticket";

	public static final String DEFAULT_CAS_SERVICE_PARAMETER = "service";

	/** Instance fields */

	private String service;

	private boolean sendRenew = false;

	private String artifactParameter = DEFAULT_CAS_ARTIFACT_PARAMETER;

	private String serviceParameter = DEFAULT_CAS_SERVICE_PARAMETER;

	/** Methods */

	public void afterPropertiesSet() throws Exception {
		Assert.hasLength(this.service, "service must be specified.");
		Assert.hasLength(this.artifactParameter, "artifactParameter cannot be empty.");
		Assert.hasLength(this.serviceParameter, "serviceParameter cannot be empty.");
	}

	/**
	 * Represents the service the user is authenticating to.
	 * <p>
	 * This service is the callback URL belonging to the local Spring Security
	 * System for Spring secured application. For example,
	 * 
	 * <pre>
	 * https://www.mycompany.com/application/j_spring_cas_security_check
	 * </pre>
	 *
	 * @return the URL of the service the user is authenticating to
	 */
	public final String getService() {

		String multitenantService = new MultitenantContextProvider().getMultitenantUrlFromUrl(this.service);

		return multitenantService;
	}

	/**
	 * Indicates whether the <code>renew</code> parameter should be sent to the
	 * CAS login URL and CAS validation URL.
	 * <p>
	 * If <code>true</code>, it will force CAS to authenticate the user again
	 * (even if the user has previously authenticated). During ticket validation
	 * it will require the ticket was generated as a consequence of an explicit
	 * login. High security applications would probably set this to
	 * <code>true</code>. Defaults to <code>false</code>, providing automated
	 * single sign on.
	 *
	 * @return whether to send the <code>renew</code> parameter to CAS
	 */
	public final boolean isSendRenew() {
		return this.sendRenew;
	}

	public final void setSendRenew(final boolean sendRenew) {
		this.sendRenew = sendRenew;
	}

	public final void setService(final String service) {
		this.service = service;
	}

	public final String getArtifactParameter() {
		return this.artifactParameter;
	}

	/**
	 * Configures the Request Parameter to look for when attempting to see if a
	 * CAS ticket was sent from the server.
	 *
	 * @param artifactParameter
	 *            the id to use. Default is "ticket".
	 */
	public final void setArtifactParameter(final String artifactParameter) {
		this.artifactParameter = artifactParameter;
	}

	/**
	 * Configures the Request parameter to look for when attempting to send a
	 * request to CAS.
	 *
	 * @return the service parameter to use. Default is "service".
	 */
	public final String getServiceParameter() {
		return this.serviceParameter;
	}

	public final void setServiceParameter(final String serviceParameter) {
		this.serviceParameter = serviceParameter;
	}
}
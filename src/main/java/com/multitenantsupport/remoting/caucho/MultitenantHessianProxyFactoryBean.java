package com.multitenantsupport.remoting.caucho;

import org.springframework.remoting.caucho.HessianProxyFactoryBean;

import com.multitenantsupport.MultitenantContextProvider;

/**
 * 
 * Esta clase extiende del cliente de Hessian para protocolo binario de servicios web
 * Redirige la URL del servicio web a la que hacer la peticion introduciendo el tenant id
 * como hostname
 *
 */
public class MultitenantHessianProxyFactoryBean extends HessianProxyFactoryBean {

	@Override
	public String getServiceUrl() {
		
		String multitenantServiceUrl = new MultitenantContextProvider().getMultitenantUrlFromUrl(super.getServiceUrl());
		
		return multitenantServiceUrl;
	}
	
	
}

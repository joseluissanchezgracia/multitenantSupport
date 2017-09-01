package com.multitenantsupport.remoting.caucho;

import java.io.IOException;
import java.net.URL;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianURLConnectionFactory;
import com.multitenantsupport.MultitenantContextProvider;

/**
 * 
 * Esta clase extiende del cliente de Hessian para protocolo binario de servicios web
 * Redirige la URL del servicio web a la que hacer la peticion introduciendo el tenant id
 * como hostname
 *
 */
public class MultitenantHessianURLConnectionFactory extends HessianURLConnectionFactory {

	@Override
	public HessianConnection open(URL url) throws IOException {
		
		// Modifica la URL si multitenancy esta habilitado
		String cadenaUrl = new MultitenantContextProvider().getMultitenantUrlFromUrl(url.toString());

		url = new URL(cadenaUrl);
		
		// return Hessian Connection
		return super.open(url);
	}

	
	
}

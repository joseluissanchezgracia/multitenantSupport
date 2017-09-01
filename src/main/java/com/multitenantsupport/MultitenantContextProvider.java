package com.multitenantsupport;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.everis.ehcos.multitenantsupport.MultitenantContextProvider;

/**
 * 
 * Implementacion por defecto del interfaz para implementacion de utilidades en entornos multitenant
 *
 */
public class MultitenantContextProvider {

	// Propiedad desde configuracion para guardar si debe tenerse en cuenta que estamos en entorno multitenant
	private static boolean multitenancyEnabled = false;

	// Logger
	private static Logger logger = LoggerFactory.getLogger(MultitenantContextProvider.class);

	/**
	 * Devuelve si esta habilitado el multitenancy
	 */
	public static boolean isMultitenantEnabled() {
		return multitenancyEnabled;
	}
	
	public static void setMultitenantEnabled(boolean multitenantEnabled) {
		multitenancyEnabled = multitenantEnabled;
	}
	
	/**
	 * Convierte una URL original a una URL que incluye el tenant Id
	 */
	public String getMultitenantUrlFromUrl(String sourceUrl) {
		
		String targetUrl = sourceUrl;
		
		// Si no esta habilitado el multitenant se devuelve la URL tal cual
		if(isMultitenantEnabled() == true)
		{
			try {
				ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
				HttpServletRequest req = sra.getRequest();
	        
	        	URL parsedSourceUrl = new URL(sourceUrl);
	        	
	        	// Se sustituye la IP o servername por el tenant Id que se compone de <subdominio de tenant>.dominio
	        	// Por ejemplo http://55.55.55.55:8080/ehEMPI pasaria a ser http://hospital1.ehcos.com:8080/ehEMPI
	        	targetUrl = new URL(req.getScheme(), req.getServerName(), req.getServerPort(), parsedSourceUrl.getFile()).toString();
	        	
			} catch (MalformedURLException e) {
				// Si se trata por ejemplo de una ruta relativa solo ponemos un warning
				logger.warn("Error al parsear como URL una cadena de caracteres: " + sourceUrl);
			} catch (IllegalStateException e) {
				// Si hay algun error al obtener la request solo ponemos un warning
				logger.warn("Error al obtener URL multitenant: " + e.getLocalizedMessage());
			}
			
		}
        
		// return
        return targetUrl;
	}

	/**
	 * Por convencion el tenant ID se obtiene del servername de la URL de la HTTP request en curso
	 * Si no se devuelve cadena vacia
	 */
	public String getCurrentTenantId() {
		String tenantId = "";
		
		if(isMultitenantEnabled() == true) {
			try {
				ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		        HttpServletRequest req = sra.getRequest();
		        tenantId = req.getServerName();
			}
			catch(IllegalStateException e) {
				// Si se trata por ejemplo de una ruta relativa solo ponemos un warning
				logger.warn("Error al obtener tenant Id. No hay HTTPrequest asociada a este hilo de la que obtenerlo");
			}
			
		}
		
		return tenantId;
	}

}

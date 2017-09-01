package com.multitenantsupport.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;

import com.multitenantsupport.MultitenantContextProvider;

/**
 * Este filtro permite inyectar en todas las cadenas de log de slf4j el tenant id
 * Para ello en el conversion pattern de un appender se puede incluir el tenant ID
 * mediante la inclusion del patron %X{tenant}
 * 
 * Por ejemplo:
 * <param name="ConversionPattern" value="%d{ABSOLUTE} %5p %X{tenant} %C,%t:%L - %m%n" />
 * 
 * @author jsanchgr
 *
 */
public class MultitenantLoggingFilter implements Filter{

	@Override
	public void destroy() {
		// Do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			
			// Si esta habilitado el contexto multitenant, se introduce la informacion
			// que necesita el MDC de log
			if( MultitenantContextProvider.isMultitenantEnabled() ) {
				String tenantId = new MultitenantContextProvider().getCurrentTenantId();
				MDC.put("tenant", tenantId);
			}
			
			// Continua la cadena de filtros
			chain.doFilter(request, response);

		} finally {
			MDC.remove("tenant");
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// Do nothing
	}

}

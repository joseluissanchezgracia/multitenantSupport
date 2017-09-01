package com.multitenantsupport.security.cas.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.multitenantsupport.MultitenantContextProvider;

import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;


/**
 * Clase de Spring 3.0.5 que hemos copiado para poder redirigir las peticiones de logout a CAS con los tenant ID correctos
 * No ha sido posible extenderla porque los metodos son finales
 * 
 * Logs a principal out.
 * <p>
 * Polls a series of {@link LogoutHandler}s. The handlers should be specified in the order they are required.
 * Generally you will want to call logout handlers <code>TokenBasedRememberMeServices</code> and
 * <code>SecurityContextLogoutHandler</code> (in that order).
 * <p>
 * After logout, a redirect will be performed to the URL determined by either the configured
 * <tt>LogoutSuccessHandler</tt> or the <tt>logoutSuccessUrl</tt>, depending on which constructor was used.
 *
 * @author Ben Alex
 */
public class LogoutFilter extends GenericFilterBean {

	    //~ Instance fields ================================================================================================

	    private String filterProcessesUrl = "/j_spring_security_logout";
	    private List<LogoutHandler> handlers;
	    private LogoutSuccessHandler logoutSuccessHandler;
	    private String logoutSuccessUrl;

	    //~ Constructors ===================================================================================================

	    public LogoutFilter(String logoutSuccessUrl, LogoutHandler... handlers) {
	        Assert.notEmpty(handlers, "LogoutHandlers are required");
	        this.handlers = Arrays.asList(handlers);
	        Assert.isTrue(!StringUtils.hasLength(logoutSuccessUrl) ||
	                UrlUtils.isValidRedirectUrl(logoutSuccessUrl), logoutSuccessUrl + " isn't a valid redirect URL");
	        SimpleUrlLogoutSuccessHandler urlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
	        if (StringUtils.hasText(logoutSuccessUrl)) {
	            urlLogoutSuccessHandler.setDefaultTargetUrl(logoutSuccessUrl);
	        }
	        this.logoutSuccessUrl = logoutSuccessUrl;
	        logoutSuccessHandler = urlLogoutSuccessHandler;
	    }

	    //~ Methods ========================================================================================================

	    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	            throws IOException, ServletException {
	        HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) res;

	        if (requiresLogout(request, response)) {
	            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	            String multitenantLogoutSuccessUrl = new MultitenantContextProvider().getMultitenantUrlFromUrl(logoutSuccessUrl);
	            ((SimpleUrlLogoutSuccessHandler) logoutSuccessHandler).setDefaultTargetUrl(multitenantLogoutSuccessUrl);
	            
	            if (logger.isDebugEnabled()) {
	                logger.debug("Logging out user '" + auth + "' and transferring to logout destination");
	            }

	            for (LogoutHandler handler : handlers) {
	                handler.logout(request, response, auth);
	            }

	            logoutSuccessHandler.onLogoutSuccess(request, response, auth);

	            return;
	        }

	        chain.doFilter(request, response);
	    }

	    /**
	     * Allow subclasses to modify when a logout should take place.
	     *
	     * @param request the request
	     * @param response the response
	     *
	     * @return <code>true</code> if logout should occur, <code>false</code> otherwise
	     */
	    protected boolean requiresLogout(HttpServletRequest request, HttpServletResponse response) {
	        String uri = request.getRequestURI();
	        int pathParamIndex = uri.indexOf(';');

	        if (pathParamIndex > 0) {
	            // strip everything from the first semi-colon
	            uri = uri.substring(0, pathParamIndex);
	        }

	        int queryParamIndex = uri.indexOf('?');

	        if (queryParamIndex > 0) {
	            // strip everything from the first question mark
	            uri = uri.substring(0, queryParamIndex);
	        }

	        if ("".equals(request.getContextPath())) {
	            return uri.endsWith(filterProcessesUrl);
	        }

	        return uri.endsWith(request.getContextPath() + filterProcessesUrl);
	    }

	    public void setFilterProcessesUrl(String filterProcessesUrl) {
	        Assert.isTrue(UrlUtils.isValidRedirectUrl(filterProcessesUrl), filterProcessesUrl + " isn't a valid value for" +
	                " 'filterProcessesUrl'");
	        this.filterProcessesUrl = filterProcessesUrl;
	    }

	    protected String getFilterProcessesUrl() {
	        return filterProcessesUrl;
	    }
	}
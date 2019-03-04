package com.challenge.urlshortener.util;


import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.challenge.urlshortener.boundary.UrlRepoResource;

@Provider
public class UlrShortenerRequestFilter implements ContainerRequestFilter {
	@Inject
    private Logger logger;// = LoggerFactory.getLogger(UlrShortenerFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    	logger.info("request entity: " + requestContext.getEntityStream());
    	for(Object resource:requestContext.getUriInfo().getMatchedResources()) {
    		/*
            if(resource instanceof UrlRepoResource) {
            	requestContext.
                //Long id = Long.valueOf(requestContext.getUriInfo().getPathParameters().getFirst("id"));
                // ...
            }
            */
        }
    	/*
        //TODO:
        for(Object resource:requestContext.getUriInfo().getMatchedResources()) {
            if(resource instanceof UrlRepoResource) {
                Long id = Long.valueOf(requestContext.getUriInfo().getPathParameters().getFirst("id"));
                // ...
            }
        }
		*/
    }
    

}
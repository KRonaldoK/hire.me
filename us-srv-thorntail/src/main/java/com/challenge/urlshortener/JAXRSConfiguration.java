package com.challenge.urlshortener;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {
	//TODO: dev
	/*
	private Set<Object> singletons = new HashSet<>();
	private Set<Class<?>> resources = new HashSet<>();
	
	public JAXRSConfiguration() {
		singletons.add(new SingletonResource());
		resources.add(UrlRepoResource.class);
	}
	
	@Override
	public Set<Object> getSingletons() {
        return singletons;
    }
	*/
}

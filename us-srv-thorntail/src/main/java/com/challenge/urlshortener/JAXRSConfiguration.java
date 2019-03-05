package com.challenge.urlshortener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {
	
	public final static String MONGODB_SEQUENCE_INIT = "mongodb-sequence-init";
	
	private Map<String, Object> props = null;
	
	@Context private ServletContext context;
	
	@Override
	public Map<String, Object> getProperties() {
		setInitParameters();
		return props;
	}
	
	public JAXRSConfiguration() {
		super();
		
	}

	private void setInitParameters() {
		if (props == null) {
			props = new HashMap<>();
			String ContextParameterValue = context.getInitParameter(MONGODB_SEQUENCE_INIT);
		    props.put(MONGODB_SEQUENCE_INIT, ContextParameterValue);
		}
	}
	
	
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

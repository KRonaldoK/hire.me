package com.challenge.urlshortener;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.challenge.urlshortener.util.BaseMapperConverter;
import com.challenge.urlshortener.util.UrlShortenerSequenceGenerator;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {
	
}

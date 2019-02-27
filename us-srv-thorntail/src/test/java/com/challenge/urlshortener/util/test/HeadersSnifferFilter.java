package com.challenge.urlshortener.util.test;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

/*
 * Filter only for development purposes
 * Utilização:
 * aClient = ClientBuilder.newBuilder()
  								.connectTimeout(30, TimeUnit.SECONDS)
								.readTimeout(30, TimeUnit.SECONDS)
								.register(JsonBindingFeature.class)
								.register(new HeadersSnifferFilter())
								.build();
 * 
 */
public class HeadersSnifferFilter implements ClientRequestFilter, ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext reqContext) throws IOException {
		System.out.println("-- Client request info --");

		log(reqContext.getUri(), reqContext.getHeaders());

	}

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

		System.out.println("-- Client response info --");
		log(requestContext.getUri(), responseContext.getHeaders());

	}

	private void log(URI uri, MultivaluedMap<String, ?> headers) {
		System.out.println("Request URI: " + uri.getPath());
		System.out.println("Headers:");
		headers.entrySet().forEach(h -> System.out.println(h.getKey() + ": " + h.getValue()));
	}
}
package com.challenge.urlshortener.util;

import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/*
 * 
 * Classe não utilizada no projeto, apenas para desenvolvimento
 */
@Provider
public class UrlShortenerApplicationEventListener implements ApplicationEventListener {
	private volatile int requestCnt = 0;

	
	@Override
	public void onEvent(ApplicationEvent event) {
		switch (event.getType()) {
		case INITIALIZATION_FINISHED:
			System.out.println("Application " + event.getResourceConfig().getApplicationName() + " was initialized.");
			break;
		case DESTROY_FINISHED:
			System.out.println("Application " + event.getResourceConfig().getApplicationName() + " destroyed.");
			break;
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		requestCnt++;
		System.out.println("Request " + requestCnt + " started.");
		// return the listener instance that will handle this request.
		return new UrlShortenerRequestEventListener(requestCnt);
	}
}
package com.challenge.urlshortener.util;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
/*
 * 
 * Classe não utilizada no projeto, apenas para desenvolvimento
 */
public class UrlShortenerRequestEventListener implements RequestEventListener {
	private final int requestNumber;
	private final long startTime;

	public UrlShortenerRequestEventListener(int requestNumber) {
		this.requestNumber = requestNumber;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onEvent(RequestEvent event) {
		switch (event.getType()) {
		case RESOURCE_METHOD_START:
			System.out.println("Resource method " + event.getUriInfo().getMatchedResourceMethod().getHttpMethod()
					+ " started for request " + requestNumber);
			break;
		case FINISHED:
			System.out.println("Request " + requestNumber + " finished. Processing time "
					+ (System.currentTimeMillis() - startTime) + " ms.");
			break;
		}
	}
}
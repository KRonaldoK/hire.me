package com.challenge.urlshortener;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;

//TODO: Just for development purposes, do not remove
@ApplicationScoped
public class ProvisioningDataForApplicationLifecycle {

	private final Map<String, Object> data = new HashMap<>();

	/*
	 * @PersistenceContext(unitName = "urlRepos") private EntityManager
	 * entityManager;
	 * 
	 * @Inject private SequenceStock sequenceStock;
	 */
	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

		System.out.println("Initialize your app here!");
	}

	public void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
		System.out.println("Shut down your resources here!");
	}
}
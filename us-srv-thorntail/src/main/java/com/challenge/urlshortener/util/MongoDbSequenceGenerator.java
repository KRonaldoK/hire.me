package com.challenge.urlshortener.util;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.core.Application;

import com.challenge.urlshortener.JAXRSConfiguration;
import com.challenge.urlshortener.domain.Sequence;
import com.challenge.urlshortener.domain.SequenceStock;
import com.challenge.urlshortener.integration.MongodbSequenceNotFoundException;

@ApplicationScoped
public class MongoDbSequenceGenerator {
	@Inject
	private SequenceStock sequenceStock;

	@Inject
	private Logger logger;

	@Inject
	private Application application;

	// Thread safe
	public synchronized long getNextUrlRepo() throws Exception {

		Sequence urlRepoSequenceRetrieved = null;

		urlRepoSequenceRetrieved = sequenceStock.findUrlRepoSeqByName();

		if (urlRepoSequenceRetrieved == null) {

			// Initialize the sequence collection
			initializeMongoDbSequence();

			urlRepoSequenceRetrieved = sequenceStock.findUrlRepoSeqByName();

			if (urlRepoSequenceRetrieved == null) {
				throw new MongodbSequenceNotFoundException("Mongodb sequence for urlrepo id was not found");
			}

		}

		long currentSeq = urlRepoSequenceRetrieved.getSeq();
		long nextSeq = currentSeq + 1;
		urlRepoSequenceRetrieved.setSeq(nextSeq);
		sequenceStock.update(urlRepoSequenceRetrieved);
		return nextSeq;

	}

	public MongoDbSequenceGenerator() {

		super();

	}

	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		
		// application was not initialized yet here, only will work for hard-coded values
		
		//logger.log(Level.INFO, "Verifying mongodb sequence for initialization...");
		

		
	}

	private void initializeMongoDbSequence() {

		Map<String, Object> props = application.getProperties();
		String initNumberString = (String) props.get(JAXRSConfiguration.MONGODB_SEQUENCE_INIT);

		Long initNumberLong = new Long(initNumberString);
		Sequence newSequence = new Sequence("url_repo_id", initNumberLong);

		sequenceStock.create(newSequence);

		logger.log(Level.INFO, "Mongodb sequence for \"UrlRepo id key\" initiated to {0}", initNumberLong);
	}
}

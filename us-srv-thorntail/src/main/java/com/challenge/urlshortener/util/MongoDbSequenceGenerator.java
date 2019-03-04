package com.challenge.urlshortener.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.challenge.urlshortener.domain.Sequence;
import com.challenge.urlshortener.domain.SequenceStock;

@ApplicationScoped
public class MongoDbSequenceGenerator {
	@Inject
	private SequenceStock sequenceStock;
	
	@Inject
	private Logger logger;

	// Thread safe
	public synchronized long getNextUrlRepo() {

		Sequence urlRepoSequenceRetrieved = null;

		urlRepoSequenceRetrieved = sequenceStock.findUrlRepoSeqByName();
		if (urlRepoSequenceRetrieved != null) {

			long currentSeq = urlRepoSequenceRetrieved.getSeq();
			long nextSeq = currentSeq + 1;
			urlRepoSequenceRetrieved.setSeq(nextSeq);
			sequenceStock.update(urlRepoSequenceRetrieved);
			return nextSeq;

		} else {

			// Initialize the sequence collection

			// It must be 1L
			Sequence newSequence = new Sequence("url_repo_id", new Long(1L));

			sequenceStock.create(newSequence);

			long currentSeq = 0;
			long nextSeq = currentSeq + 1;
			return nextSeq;
		}

	}

	public MongoDbSequenceGenerator() {
		
		super();
	}
	/*
	public static MongoDbSequenceGenerator getInstance() {

		return INSTANCE;
	}
	*/
	public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
		logger.log(Level.INFO, "Verifying mongodb sequence for initialization...");
		
	}
}

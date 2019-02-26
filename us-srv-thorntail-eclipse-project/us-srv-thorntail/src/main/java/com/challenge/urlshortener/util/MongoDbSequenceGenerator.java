package com.challenge.urlshortener.util;

import com.challenge.urlshortener.domain.Sequence;
import com.challenge.urlshortener.domain.SequenceStock;

public class MongoDbSequenceGenerator {

	private SequenceStock sequenceStock;

	public SequenceStock getSequenceStock() {
		return sequenceStock;
	}

	public void setSequenceStock(SequenceStock sequenceStock) {
		this.sequenceStock = sequenceStock;
	}

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

}

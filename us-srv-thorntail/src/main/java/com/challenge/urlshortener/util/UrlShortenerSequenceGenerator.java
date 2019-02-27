package com.challenge.urlshortener.util;

import com.challenge.urlshortener.domain.SequenceStock;

/**
 * Generates the UrlRepo idKey
 */

public final class UrlShortenerSequenceGenerator {

	public static final UrlShortenerSequenceGenerator INSTANCE = new UrlShortenerSequenceGenerator();

	public static MongoDbSequenceGenerator sequenceGenerator;

	public final long getNextUlrRepo(SequenceStock sequenceStock) {
		return sequenceGenerator.getNextUrlRepo(sequenceStock);
	}

	public static MongoDbSequenceGenerator getSequenceGenerator() {
		return sequenceGenerator;
	}

	private UrlShortenerSequenceGenerator() {

		setUp();

	}

	public static UrlShortenerSequenceGenerator getInstance() {

		return INSTANCE;
	}

	public void setUp() {

		sequenceGenerator = MongoDbSequenceGenerator.getInstance();
	}

}

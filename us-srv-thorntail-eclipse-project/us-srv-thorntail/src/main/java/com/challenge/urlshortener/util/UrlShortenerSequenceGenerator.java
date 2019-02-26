package com.challenge.urlshortener.util;

import com.challenge.urlshortener.domain.SequenceStock;

/**
 * Generates the UrlRepo idKey
 */

public final class UrlShortenerSequenceGenerator {

	public static final UrlShortenerSequenceGenerator INSTANCE = new UrlShortenerSequenceGenerator();

	public static MongoDbSequenceGenerator sequenceGenerator;

	public final long getNextUlrRepo() {
		return sequenceGenerator.getNextUrlRepo();
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

		sequenceGenerator = new MongoDbSequenceGenerator();
	}

	public void setSequenceStock(SequenceStock sequenceStock) {
		sequenceGenerator.setSequenceStock(sequenceStock);
	}

}

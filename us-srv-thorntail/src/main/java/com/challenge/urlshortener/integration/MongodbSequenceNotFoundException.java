package com.challenge.urlshortener.integration;

import com.challenge.urlshortener.domain.UrlRepo;

public class MongodbSequenceNotFoundException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;
	
	private UrlRepo urlRepo;
	
	public UrlRepo getUrlRepo() {
		return urlRepo;
	}

	public MongodbSequenceNotFoundException() {
		super();
	}

	public MongodbSequenceNotFoundException(String message) {
		super(message);
	}
	
	public MongodbSequenceNotFoundException(String message, UrlRepo urlRepo) {
		super(message);
		this.urlRepo = urlRepo;
	}

}
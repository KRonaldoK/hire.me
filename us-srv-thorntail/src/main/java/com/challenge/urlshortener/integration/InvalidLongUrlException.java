package com.challenge.urlshortener.integration;

import com.challenge.urlshortener.domain.UrlRepo;

public class InvalidLongUrlException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;
	
	private UrlRepo urlRepo;
	
	public UrlRepo getUrlRepo() {
		return urlRepo;
	}

	public InvalidLongUrlException() {
		super();
	}

	public InvalidLongUrlException(String message) {
		super(message);
	}
	
	public InvalidLongUrlException(String message, UrlRepo urlRepo) {
		super(message);
		this.urlRepo = urlRepo;
	}

}
package com.challenge.urlshortener.integration;

public class UrlRepoMissingAttributeException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;

	public UrlRepoMissingAttributeException() {
		super();
	}

	public UrlRepoMissingAttributeException(String message) {
		super(message);
	}

}
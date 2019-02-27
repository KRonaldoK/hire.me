package com.challenge.urlshortener.integration;

public class UrlRepoMissingShortUrlException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;

	public UrlRepoMissingShortUrlException() {
		super();
	}

	public UrlRepoMissingShortUrlException(String message) {
		super(message);
	}

}
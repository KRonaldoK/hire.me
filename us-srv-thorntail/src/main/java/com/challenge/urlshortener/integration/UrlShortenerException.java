package com.challenge.urlshortener.integration;

public class UrlShortenerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UrlShortenerException() {
		super();
	}

	public UrlShortenerException(String message) {
		super(message);
	}

	public UrlShortenerException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UrlShortenerException(Throwable cause) {
		super(cause);
	}
}


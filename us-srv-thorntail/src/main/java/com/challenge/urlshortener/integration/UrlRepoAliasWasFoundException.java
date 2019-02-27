package com.challenge.urlshortener.integration;

import com.challenge.urlshortener.domain.UrlRepo;

public class UrlRepoAliasWasFoundException extends UrlShortenerException {

	private static final long serialVersionUID = 1L;
	
	private UrlRepo urlRepo;
	
	public UrlRepo getUrlRepo() {
		return urlRepo;
	}

	public UrlRepoAliasWasFoundException() {
		super();
	}

	public UrlRepoAliasWasFoundException(String message) {
		super(message);
	}
	
	public UrlRepoAliasWasFoundException(String message, UrlRepo urlRepo) {
		super(message);
		this.urlRepo = urlRepo;
	}

}
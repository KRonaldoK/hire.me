package com.challenge.urlshortener.boundary;

import com.challenge.urlshortener.domain.UrlRepo;


public class AlreadyGeneratedShortUrlCommand extends ShortenUrlCommand {
	
	private final UrlRepo urlRepo;

	public AlreadyGeneratedShortUrlCommand(UrlRepo urlRepo) {
		super();
		this.urlRepo = urlRepo;
	}

	@Override
	public UrlRepo execute() {

		return this.urlRepo;
	}

}

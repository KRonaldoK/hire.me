package com.challenge.urlshortener.boundary;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.integration.UrlRepoMissingAttributeException;

@RequestScoped
public class UrlShortenerService {

	@Inject
	private Logger logger;

	@Inject
	private ShortenUrlCommandFactory shortenUrlCommandFactory;

	public UrlRepo shortenUrl(UrlRepo urlRepo, UriInfo uriInfo) throws Exception {

		validate(urlRepo);

		String customAlias = urlRepo.getAlias();

		ShortenUrlCommand shortenUrlCommand = shortenUrlCommandFactory.create(urlRepo, customAlias, uriInfo);

		UrlRepo urlRepoReturned = shortenUrlCommand.execute();

		return urlRepoReturned;

	}

	public void validate(UrlRepo urlRepo) {

		logger.log(Level.INFO, "validating {0}.", urlRepo);

		try {
			Objects.requireNonNull(urlRepo);
		} catch (NullPointerException e) {
			throw new UrlRepoMissingAttributeException("Faltam informações para o encurtamento da url.");
		}

		try {
			Objects.requireNonNull(urlRepo.getLongUrl());
		} catch (NullPointerException e) {
			throw new UrlRepoMissingAttributeException("Não foi informada a url para encurtamento.");
		}

	}
}

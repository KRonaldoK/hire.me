package com.challenge.urlshortener.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.integration.InvalidLongUrlException;
import com.challenge.urlshortener.util.UrlConverter;
import com.challenge.urlshortener.util.UrlValidator;

@RequestScoped
public class CreateShortUrlWithCustomAliasCommand extends ShortenUrlCommand {

	@Inject
	Logger logger;
	@Inject
	UrlConverter urlConverter;
	@Inject
	UrlRepoStock urlRepoStock;

	private UrlRepo urlRepo;
	private final UriInfo uriInfo;
	private final String customAlias;

	public CreateShortUrlWithCustomAliasCommand(UrlRepo urlRepo, UriInfo uriInfo, String customAlias
			, Logger logger, UrlConverter urlConverter, UrlRepoStock urlRepoStock) {
		super();
		this.urlRepo = urlRepo;
		this.uriInfo = uriInfo;
		this.customAlias = customAlias;
		this.logger = logger;
		this.urlConverter = urlConverter;
		this.urlRepoStock = urlRepoStock;
		
		
	}

	@Override
	public UrlRepo execute() throws Exception {
		// Always create a short url, even if the long url is already at the repository

		logger.log(Level.INFO, "Alias was passed: {0}", urlRepo.getAlias());

		// Valida e encurta a url longa com o custom alias
		urlRepo = handleLongUrlWithCustomAlias(urlRepo, uriInfo, logger, urlConverter, urlRepoStock);
		
		// persist
		urlRepoStock.create(urlRepo);
		
		logger.log(Level.INFO, "UrlRepo persisted: {0}", urlRepo);

		return urlRepo;

	}

	private UrlRepo handleLongUrlWithCustomAlias(UrlRepo urlRepo, UriInfo uriInfo, Logger logger, UrlConverter urlConverter, UrlRepoStock urlRepoStock) throws InvalidLongUrlException {
		String longUrl = urlRepo.getLongUrl();

		logger.log(Level.INFO, "Received url to shorten: {0}", longUrl);

		if (UrlValidator.INSTANCE.validateURL(longUrl)) {

			logger.log(Level.INFO, "longUrl validated");

			//String localURL = uriInfo.getAbsolutePath().toString();

			UrlRepo filledUrlRepo = urlConverter.shortenURLWithCustomAlias(uriInfo, customAlias, urlRepo, logger);

			logger.log(Level.INFO, "Shortened url to: {0}", filledUrlRepo.getShortUrl());

			return filledUrlRepo;

		} else {
			throw new InvalidLongUrlException();
		}
	}
}

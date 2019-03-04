package com.challenge.urlshortener.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriInfo;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.integration.InvalidLongUrlException;
import com.challenge.urlshortener.util.UrlConverter;
import com.challenge.urlshortener.util.UrlValidator;

public class CreateShortUrlWithNoCustomAliasCommand extends ShortenUrlCommand {
	
	private final UrlRepo urlRepo;
	private final UriInfo uriInfo;
	private Logger logger;
	private UrlRepoStock urlRepoStock;
	private UrlConverter urlConverter;
	
	public CreateShortUrlWithNoCustomAliasCommand(UrlRepo urlRepo, UriInfo uriInfo
			, Logger logger, UrlRepoStock urlRepoStock, UrlConverter urlConverter) {
		super();
		this.urlRepo = urlRepo;
		this.uriInfo = uriInfo;
		this.logger = logger;
		this.urlRepoStock = urlRepoStock;
		this.urlConverter = urlConverter;
	}

	@Override
	public UrlRepo execute() throws Exception {
		
		// Valida e encurta a url longa traduzindo o id key da url da base 10 para a base 62
		UrlRepo newUrlRepo = handleLongUrl(urlRepo, uriInfo);
		
		// persist
		urlRepoStock.create(newUrlRepo);

		logger.log(Level.INFO, "UrlRepo persisted: {0}", urlRepo);
		
		return null;
	}
	
	private UrlRepo handleLongUrl(UrlRepo urlRepo, UriInfo uriInfo) throws Exception {
		String longUrl = urlRepo.getLongUrl();

		logger.log(Level.INFO, "Received url to shorten: {0}", longUrl);

		if (UrlValidator.INSTANCE.validateURL(longUrl)) {
			
			UrlRepo filledUrlRepo = urlConverter.shortenURLWithNoCustomAlias(uriInfo, urlRepo);

			logger.log(Level.INFO, "Shortened url to: {0}", filledUrlRepo.getShortUrl());

			// retorn a url já encurtada
			return filledUrlRepo;

		} else {
			throw new InvalidLongUrlException();
		}
	}
}

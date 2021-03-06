package com.challenge.urlshortener.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;

@RequestScoped
public class UrlConverter {

	@Inject
	private UrlRepoStock urlRepoStock;

	@Inject
	private Logger logger;

	@Inject
	private MongoDbSequenceGenerator mongoDbSequenceGenerator;

	public UrlConverter() {

	}

	/**
	 * Shortens the long url and persists the urlRepo
	 *
	 * @param localURL the localURL
	 * @param longUrl  the longUrl
	 * @return the shortened url
	 * @throws Exception
	 */
	public UrlRepo shortenURLWithNoCustomAlias(UriInfo uriInfo, UrlRepo urlRepo) throws Exception {

		logger.log(Level.INFO, "Shortening {0}", urlRepo.getLongUrl());

		String newAliasToBeGenerated = null;
		// alias na base 10
		Long idKey = -1L;

		// did someone informed before, a custom alias that causes collision with this
		// new generated alias?
		boolean aliasAlreadyExists = false;

		do {

			idKey = mongoDbSequenceGenerator.getNextUrlRepo();

			newAliasToBeGenerated = BaseMapperConverter.getInstance().createUniqueID(idKey);

			if (urlRepoStock.exists(newAliasToBeGenerated)) {
				aliasAlreadyExists = true;
			} else {
				aliasAlreadyExists = false;
			}

		} while (aliasAlreadyExists);
		
		String localURL = uriInfo.getAbsolutePath().toString();
		
		//httpRequest.getRequestURL().toString();
		
		String shortenedURL = localURL + "/" + newAliasToBeGenerated;

		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);

		// setting attributes to be persisted
		urlRepo.setAlias(newAliasToBeGenerated);
		urlRepo.setIdKey(idKey);
		urlRepo.setShortUrl(shortenedURL);

		return urlRepo;

	}

	public UrlRepo shortenURLWithCustomAlias(UriInfo uriInfo, String customAlias, UrlRepo urlRepo, Logger logger) {

		logger.log(Level.INFO, "Shortening {0}", urlRepo.getLongUrl());
		
		String localUrl = uriInfo.getAbsolutePath().toString();
		
		String shortenedURL = localUrl + "/" + customAlias;

		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);
		
		urlRepo.setAlias(customAlias);
		urlRepo.setShortUrl(shortenedURL);

		return urlRepo;

	}

}
package com.challenge.urlshortener.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;

@RequestScoped
public class UrlShortener {

	@Inject
	private UrlRepoStock urlRepoStock;

	@Inject
	private Logger logger;

	@Inject
	private MongoDbSequenceGenerator mongoDbSequenceGenerator;

	private UrlRepo urlRepo;

	public void setUrlRepo(UrlRepo urlRepo) {
		this.urlRepo = urlRepo;
	}

	public UrlShortener() {

	}

	/**
	 * Shortens the long url and persists the urlRepo
	 *
	 * @param localURL the localURL
	 * @param longUrl  the longUrl
	 * @return the shortened url
	 * @throws Exception
	 */
	public UrlRepo shortenURL(String localURL, String longUrl) throws Exception {

		logger.log(Level.INFO, "Shortening {0}", longUrl);

		String uniqueAlias = null;
		Long idKey = -1L;

		// did someone informed before, a custom alias that causes collision with this
		// new generated alias?
		boolean aliasAlreadyExists = false;
		
		do {

			idKey = mongoDbSequenceGenerator.getNextUrlRepo();

			uniqueAlias = BaseMapperConverter.getInstance().createUniqueID(idKey);

			if (urlRepoStock.exists(uniqueAlias)) {
				aliasAlreadyExists = true;
			} else {
				aliasAlreadyExists = false;
			}

		} while (aliasAlreadyExists);

		String shortenedURL = localURL + "/" + uniqueAlias;

		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);

		// setting attributes to be persisted
		urlRepo.setAlias(uniqueAlias);
		urlRepo.setIdKey(idKey);
		urlRepo.setShortUrl(shortenedURL);

		return urlRepo;

	}
	
	public UrlRepo shortenURLWithCustomAlias(String localURL, String customAlias) {

		logger.log(Level.INFO, "Shortening {0}", urlRepo.getLongUrl());

		String shortenedURL = localURL + "/" + customAlias;

		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);

		urlRepo.setShortUrl(shortenedURL);

		return urlRepo;

	}

}
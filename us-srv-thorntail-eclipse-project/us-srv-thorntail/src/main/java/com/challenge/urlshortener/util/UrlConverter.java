package com.challenge.urlshortener.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.challenge.urlshortener.domain.SequenceStock;
import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;


public class UrlConverter {
	
	private Logger logger = Logger.getLogger(UrlConverter.class.getName());
	
	private final UrlRepo urlRepo;

	public UrlConverter(UrlRepo urlrepo) {
		
		this.urlRepo = urlrepo;
		
	}
	
	/**
	 * Shortens the long url and persists the urlRepo
	 *
	 * @param localURL the localURL
	 * @param longUrl the longUrl
	 * @return the shortened url
	 * @throws Exception 
	 */
	public UrlRepo shortenURL(String localURL, String longUrl, SequenceStock sequenceStock, UrlRepoStock urlRepoStock) throws Exception {

		logger.log(Level.INFO, "Shortening {0}", longUrl);

		UrlShortenerSequenceGenerator urlShortenerSequenceGenerator = UrlShortenerSequenceGenerator.getInstance();
		
		String uniqueID = null;
		Long idKey = -1L;
		
		// did someone informed before, a custom alias that causes collision with this new generated alias? 
		boolean aliasAlreadyExists = false;
		
		do {
			
			idKey = urlShortenerSequenceGenerator.getNextUlrRepo(sequenceStock);
			
			uniqueID = BaseMapperConverter.getInstance().createUniqueID(idKey);
			
			if (urlRepoStock.exists(uniqueID)) {
				aliasAlreadyExists = true;
			} else{
				aliasAlreadyExists = false;
			}
			
		} while (aliasAlreadyExists);
		
		String shortenedURL = localURL +"/" + uniqueID;
		
		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);
		
		// setting attributes to be persisted
		urlRepo.setAlias(uniqueID);
		urlRepo.setIdKey(idKey);
		urlRepo.setShortUrl(shortenedURL);
		
		return urlRepo;

	}
	
	public UrlRepo shortenURLWithCustomAlias(String localURL, String customAlias) {

		logger.log(Level.INFO, "Shortening {0}", urlRepo.getLongUrl());

		String shortenedURL = localURL +"/" + customAlias;
		
		logger.log(Level.INFO, "Shortened url is: {0}", shortenedURL);
		
		urlRepo.setShortUrl(shortenedURL);
		
		return urlRepo;

	}
	
	/*
	private String formatLocalURLFromShortener(String localURL, String uniqueID) {
		
		return localURL + "/" +uniqueID;

	}
	*/
}
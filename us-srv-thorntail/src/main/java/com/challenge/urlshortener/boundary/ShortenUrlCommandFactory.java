package com.challenge.urlshortener.boundary;

import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.integration.UrlRepoAliasWasFoundException;
import com.challenge.urlshortener.util.UrlConverter;

@ApplicationScoped
public class ShortenUrlCommandFactory {

	@Inject
	private Logger logger;
	@Inject
	private UrlRepoStock urlRepoStock;
	@Inject
	private UrlConverter urlConverter;

	public ShortenUrlCommand create(UrlRepo urlRepo, String customAlias, UriInfo uriInfo) {

		ShortenUrlCommand shortUrlCommand;

		if (wasInformed(customAlias)) {
			if (urlRepoStock.exists(urlRepo.getAlias())) {
				// CUSTOM ALIAS ALREADY EXISTS
				String message = "CUSTOM ALIAS ALREADY EXISTS";
				throw new UrlRepoAliasWasFoundException(message, urlRepo);

			}
			// Nova url curta com custom alias
			shortUrlCommand = createCreateShortUrlWithCustomAliasCommand(urlRepo, uriInfo, customAlias, logger,
					urlConverter, urlRepoStock);
		} else {
			// Nova url curta "sem custom alias"
			
			// was the long url shortened before?
			UrlRepo urlRepoAlreadyGenerated = urlRepoStock.findGeneratedShortUrlByLongUrl(urlRepo.getLongUrl());

			if (urlRepoAlreadyGenerated == null) {
				// Nova url curta "sem custom alias" a ser gerada
				shortUrlCommand = createCreateShortUrlWithNoCustomAliasCommand(urlRepo, uriInfo, logger, urlRepoStock,
						urlConverter);
			} else {
				// re-utiliza do repositorio, a url curta já criada a partir da url longa 
				shortUrlCommand = createAlreadyGeneratedShortUrlCommand(urlRepoAlreadyGenerated);
			}
		}

		return shortUrlCommand;
	}

	private boolean wasInformed(String alias) {
		String s = Optional.ofNullable(alias).orElse("");
		return !s.trim().isEmpty();
	}

	public CreateShortUrlWithNoCustomAliasCommand createCreateShortUrlWithNoCustomAliasCommand(UrlRepo urlRepo,
			UriInfo uriInfo, Logger logger, UrlRepoStock urlRepoStock, UrlConverter urlConverter) {

		return new CreateShortUrlWithNoCustomAliasCommand(urlRepo, uriInfo, logger, urlRepoStock, urlConverter);

	};

	public AlreadyGeneratedShortUrlCommand createAlreadyGeneratedShortUrlCommand(UrlRepo urlRepo) {
		return new AlreadyGeneratedShortUrlCommand(urlRepo);
	};

	public CreateShortUrlWithCustomAliasCommand createCreateShortUrlWithCustomAliasCommand(UrlRepo urlRepo,
			UriInfo uriInfo, String customAlias, Logger logger, UrlConverter urlConverter, UrlRepoStock urlRepoStock) {
		return new CreateShortUrlWithCustomAliasCommand(urlRepo, uriInfo, customAlias, logger, urlConverter,
				urlRepoStock);
	};
}

package com.challenge.urlshortener.boundary;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.challenge.urlshortener.domain.SequenceStock;
import com.challenge.urlshortener.domain.Statistics;
import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.integration.InvalidLongUrlException;
import com.challenge.urlshortener.integration.UrlRepoAliasWasFoundException;
import com.challenge.urlshortener.integration.UrlRepoMissingAttributeException;
import com.challenge.urlshortener.integration.UrlRepoMissingShortUrlException;
import com.challenge.urlshortener.util.UrlConverter;
import com.challenge.urlshortener.util.UrlShortenerApplicationEventListener;
import com.challenge.urlshortener.util.UrlValidator;

/**
 * The UrlRepo REST resource implementation.
 */
@Path("urlRepository")
@RequestScoped
@Provider
public class UrlRepoResource {

	@Inject
	private UrlRepoStock urlRepoStock;
	
	@Inject
	private SequenceStock sequenceStock;

	@Inject
	private Logger logger;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(UrlRepo urlRepo, @Context HttpServletRequest httpRequest) throws Exception {

		logger.log(Level.INFO, "Initiating url shortening: {0}", urlRepo);
		
		// compute statistics
		long startTime = System.currentTimeMillis();

		validate(urlRepo);

		String theAlias = urlRepo.getAlias();

		// verifica se o custom alias foi informado

		if (wasInformed(theAlias)) {
			// custom alias já existe no repositorio de url?
			if (urlRepoStock.exists(urlRepo.getAlias())) {

				String message = "CUSTOM ALIAS ALREADY EXISTS";
				throw new UrlRepoAliasWasFoundException(message, urlRepo);

			} else {

				// Utiliza o custom alias informado
				urlRepo.setAlias(theAlias);

				logger.log(Level.INFO, "Alias was passed: {0}", urlRepo.getAlias());

				UrlRepo urlRepoRetrieved = urlRepoStock.findByLongUrl(urlRepo.getLongUrl());

				// Nova url longa
				if (urlRepoRetrieved == null) {

					// Valida e encurta a url longa com o custom alias
					urlRepo = handleLongUrlWithCustomAlias(urlRepo, httpRequest);

					urlRepoStock.create(urlRepo);

					logger.log(Level.INFO, "UrlRepo persisted: {0}", urlRepo);

				} else {
					// retorna a url já existente do repositorio
					urlRepo = urlRepoRetrieved;
				}

			}
			// custom alias não foi informado
		} else {
			logger.log(Level.INFO, "Alias was not passed: {0}", urlRepo.getAlias());

			UrlRepo urlRepoRetrieved = urlRepoStock.findByLongUrl(urlRepo.getLongUrl());

			// Nova url longa
			if (urlRepoRetrieved == null) {

				// Valida e encurta a url longa passando o id key da url da base 10 para a base
				// 62
				urlRepo = handleLongUrl(urlRepo, httpRequest, sequenceStock, urlRepoStock);

				urlRepoStock.create(urlRepo);

				logger.log(Level.INFO, "UrlRepo persisted: {0}", urlRepo);

			} else {
				// retorna a url já existente do repositorio
				urlRepo = urlRepoRetrieved;
			}

		}

		// compute statistics
		long timeElapsed = System.currentTimeMillis() - startTime;

		Statistics statistics = new Statistics();
		statistics.setTimeTaken(timeElapsed + "ms");

		urlRepo.setStatistics(statistics);

		return Response.status(Status.CREATED).entity(urlRepo).type(MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/{alias}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("alias") String alias, @Context HttpServletRequest httpRequest) {
		
		String shortenedURL = httpRequest.getRequestURL().toString();
		
		// pesquisar a url curta
		UrlRepo urlRepo = urlRepoStock.findByShortUrl(shortenedURL);
		if (urlRepo == null) {

			throw new UrlRepoMissingShortUrlException("SHORTENED URL NOT FOUND");
			
		}

		// TODO: computar quantidade de acessos no "get" do alias? requestListener?
		// planeta.computeAparicoes();

		return Response.status(Status.TEMPORARY_REDIRECT).entity(urlRepo).type(MediaType.APPLICATION_JSON)
				.header("Location", urlRepo.getLongUrl()).build();

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

	private UrlRepo handleLongUrl(UrlRepo urlRepo, HttpServletRequest httpRequest, SequenceStock sequenceStock, UrlRepoStock urlRepoStock) throws Exception {
		String longUrl = urlRepo.getLongUrl();

		logger.log(Level.INFO, "Received url to shorten: {0}", longUrl);

		if (UrlValidator.INSTANCE.validateURL(longUrl)) {

			String localURL = httpRequest.getRequestURL().toString();

			UrlRepo filledUrlRepo = new UrlConverter(urlRepo).shortenURL(localURL, longUrl, sequenceStock, urlRepoStock);

			logger.log(Level.INFO, "Shortened url to: {0}", filledUrlRepo.getShortUrl());

			// retorn a url já encurtada
			return filledUrlRepo;

		} else {
			throw new InvalidLongUrlException();
		}
	}

	private UrlRepo handleLongUrlWithCustomAlias(UrlRepo urlRepo, HttpServletRequest httpRequest)
			throws InvalidLongUrlException {
		String longUrl = urlRepo.getLongUrl();

		logger.log(Level.INFO, "Received url to shorten: {0}", longUrl);

		if (UrlValidator.INSTANCE.validateURL(longUrl)) {

			logger.log(Level.INFO, "longUrl validated");

			String localURL = httpRequest.getRequestURL().toString();

			UrlRepo filledUrlRepo = new UrlConverter(urlRepo).shortenURLWithCustomAlias(localURL, urlRepo.getAlias());

			logger.log(Level.INFO, "Shortened url to: {0}", filledUrlRepo.getShortUrl());

			return filledUrlRepo;

		} else {
			throw new InvalidLongUrlException();
		}
	}

	private boolean wasInformed(String alias) {
		String s = Optional.ofNullable(alias).orElse("");
		return !s.trim().isEmpty();
	}

	public UrlRepoResource() {
		super();
	}

	///////////////////////////////////////
	
	/*
	private volatile int requestCnt = 0;

	@Override
	public void onEvent(ApplicationEvent event) {
		switch (event.getType()) {
		case INITIALIZATION_FINISHED:
			System.out.println("Application " + event.getResourceConfig().getApplicationName() + " was initialized.");
			break;
		case DESTROY_FINISHED:
			System.out.println("Application " + event.getResourceConfig().getApplicationName() + " destroyed.");
			break;
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent requestEvent) {
		requestCnt++;
		System.out.println("Request " + requestCnt + " started.");

		// return the listener instance that will handle this request.
		logger.log(Level.INFO, "return the listener instance that will handle this request.");
		
		return new UrlRepoResource(requestCnt);
	}
	*/
	///////////////////////////////////
	/*
	private int requestNumber;
	private long startTime;
	private long timeElapsed;

	public UrlRepoResource(int requestNumber) {
		this.requestNumber = requestNumber;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onEvent(RequestEvent event) {
		switch (event.getType()) {
		case RESOURCE_METHOD_START:
			System.out.println("Resource method " + event.getUriInfo().getMatchedResourceMethod().getHttpMethod()
					+ " started for request " + requestNumber);
			break;
		case FINISHED:
			timeElapsed = (System.currentTimeMillis() - startTime);
			System.out.println("Request " + requestNumber + " finished. Processing time " + timeElapsed + " ms.");
			break;
		}
	}
	*/
}

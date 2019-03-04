package com.challenge.urlshortener.boundary;

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
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.challenge.urlshortener.domain.Statistics;
import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.integration.UrlRepoMissingShortUrlException;

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
	private Logger logger;

	@Inject
	private UrlShortenerService urlShortenerService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(UrlRepo urlRepo, @Context UriInfo uriInfo) throws Exception {

		logger.log(Level.INFO, "Initiating url shortening: {0}", urlRepo);

		// compute statistics
		long startTime = System.currentTimeMillis();

		urlShortenerService.shortenUrl(urlRepo, uriInfo);

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

	public UrlRepoResource() {
		super();
	}

	///////////////////////////////////////

	/*
	 * private volatile int requestCnt = 0;
	 * 
	 * @Override public void onEvent(ApplicationEvent event) { switch
	 * (event.getType()) { case INITIALIZATION_FINISHED:
	 * System.out.println("Application " +
	 * event.getResourceConfig().getApplicationName() + " was initialized."); break;
	 * case DESTROY_FINISHED: System.out.println("Application " +
	 * event.getResourceConfig().getApplicationName() + " destroyed."); break; } }
	 * 
	 * @Override public RequestEventListener onRequest(RequestEvent requestEvent) {
	 * requestCnt++; System.out.println("Request " + requestCnt + " started.");
	 * 
	 * // return the listener instance that will handle this request.
	 * logger.log(Level.INFO,
	 * "return the listener instance that will handle this request.");
	 * 
	 * return new UrlRepoResource(requestCnt); }
	 */
	///////////////////////////////////
	/*
	 * private int requestNumber; private long startTime; private long timeElapsed;
	 * 
	 * public UrlRepoResource(int requestNumber) { this.requestNumber =
	 * requestNumber; startTime = System.currentTimeMillis(); }
	 * 
	 * @Override public void onEvent(RequestEvent event) { switch (event.getType())
	 * { case RESOURCE_METHOD_START: System.out.println("Resource method " +
	 * event.getUriInfo().getMatchedResourceMethod().getHttpMethod() +
	 * " started for request " + requestNumber); break; case FINISHED: timeElapsed =
	 * (System.currentTimeMillis() - startTime); System.out.println("Request " +
	 * requestNumber + " finished. Processing time " + timeElapsed + " ms."); break;
	 * } }
	 */
}

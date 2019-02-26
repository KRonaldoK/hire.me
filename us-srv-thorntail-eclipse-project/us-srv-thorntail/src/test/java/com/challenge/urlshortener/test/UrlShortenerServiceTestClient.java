package com.challenge.urlshortener.test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jsonb.JsonBindingFeature;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.util.test.RandomStringTest;

/**
 * A standalone JAX-RS client implementation for the url shortener service.
 */
public class UrlShortenerServiceTestClient {

	private static final Logger LOGGER = Logger.getAnonymousLogger();

	public static void main(String[] args) {
		// construct a JAX-RS client using the builder
		Client client = null;
		try {
			client = ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS)
					.register(JsonBindingFeature.class).build();

			// construct a web target for the url shortener service
			WebTarget api = client.target("http://localhost:8080").path("/api");

			//
			// Shorten URL: Chamada sem CUSTOM_ALIAS
			//

			UrlRepo urlRepo = new UrlRepo();
			urlRepo.setLongUrl(
					"https://www.msn.com/pt-br/noticias/loterias/sorteio-extra-da-mega-sena-pode-pagar-rdollar-43-milhões-nesta-terça-26/ar-BBU6wiY?ocid=spartanntp");
			LOGGER.log(Level.INFO, "Entity.json(urlRepo)=[{0}]", Entity.json(urlRepo).toString());
			LOGGER.log(Level.INFO, "Creating new {0}.", urlRepo.toString());
			Response response = api.path("/urlRepository").request(MediaType.APPLICATION_JSON)
					.post(Entity.json(urlRepo));
			LOGGER.log(Level.INFO, "response.getStatus()=[{0}]", response.getStatus());
			UrlRepo urlRepoAlreadyCreated = response.readEntity(UrlRepo.class);
			LOGGER.log(Level.INFO, "Created UrlRepo={0}.", urlRepoAlreadyCreated.toString());
			assert response.getStatus() == 201;

			//
			// Shorten URL: Chamada com CUSTOM_ALIAS
			//
			urlRepo = new UrlRepo();
			RandomStringTest gen = new RandomStringTest(8, ThreadLocalRandom.current());
			String customAlias = gen.nextString();
			urlRepo.setAlias(customAlias);
			urlRepo.setLongUrl("http://www.google.com");
			LOGGER.log(Level.INFO, "Creating new {0}.", urlRepo.toString());
			LOGGER.log(Level.INFO, "Entity.json(urlRepo)=[{0}]", Entity.json(urlRepo).toString());
			response = api.path("/urlRepository").request(MediaType.APPLICATION_JSON).post(Entity.json(urlRepo));
			LOGGER.log(Level.INFO, "response.getStatus()=[{0}]", response.getStatus());

			assert response.getStatus() == 201;

			//
			// Shorten URL: Chamada com CUSTOM_ALIAS que já existe
			//

			urlRepo = new UrlRepo();
			// Custom alias reutilizado de "Shorten URL: Chamada com CUSTOM_ALIAS"
			urlRepo.setAlias(customAlias);
			urlRepo.setLongUrl("https://codahale.com/what-makes-jersey-interesting-injection-providers/");
			LOGGER.log(Level.INFO, "Entity.json(urlRepo)=[{0}]", Entity.json(urlRepo).toString());
			LOGGER.log(Level.INFO, "Creating new {0}.", urlRepo.toString());
			response = api.path("/urlRepository").request(MediaType.APPLICATION_JSON).post(Entity.json(urlRepo));
			LOGGER.log(Level.INFO, "response.getStatus()=[{0}]", response.getStatus());
			assert response.getStatus() == 409;

			//
			// Retrieve URL: SHORTENED URL NOT FOUND
			//
			LOGGER.log(Level.INFO, "Get unknown url repo by alias.");
			response = api.path("/urlRepository").path("/{alias}").resolveTemplate("alias", "2_1_2").request()
					.accept(MediaType.APPLICATION_JSON).get();
			LOGGER.log(Level.INFO, "response.getStatus()=[{0}]", response.getStatus());

			assert response.getStatus() == 404;

			//
			// Retrieve URL: REDIRECT
			//
			LOGGER.log(Level.INFO, "Get the long url from the short url");
			String aliasAlreadyCreated = urlRepoAlreadyCreated.getAlias();
			response = api.path("/urlRepository").path("/{alias}").resolveTemplate("alias", aliasAlreadyCreated)
					.request().accept(MediaType.APPLICATION_JSON).get();
			LOGGER.log(Level.INFO, "response.getStatus()=[{0}]", response.getStatus());

			assert response.getStatus() == 307;

			UrlRepo urlRepoToBeRedirected = response.readEntity(UrlRepo.class);
			LOGGER.log(Level.INFO, "Created UrlRepo={0}.", urlRepoToBeRedirected.toString());

		} catch (Exception e) {
			// Just to debug
			e.printStackTrace();
		} finally {
			if (client != null) {
				client.close();
			}
		}
	}

	// Just for development purposes
	private static GenericType<List<UrlRepo>> List() {
		return new GenericType<List<UrlRepo>>() {
		};
	}

}

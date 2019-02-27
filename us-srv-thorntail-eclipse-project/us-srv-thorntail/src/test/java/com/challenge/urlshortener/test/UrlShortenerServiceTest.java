package com.challenge.urlshortener.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jsonb.JsonBindingFeature;
//import org.glassfish.jersey.jsonb.JsonBindingFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.util.test.RandomStringTest;

public class UrlShortenerServiceTest {
	
	private WebTarget apiRedirect;
	private WebTarget apiShorten;
	
	private Client client;

	private String createdCustomAlias = "b";

	@Before
	public void setUp() {
		client = ClientBuilder.newBuilder()
				.connectTimeout(10, TimeUnit.SECONDS)
				.readTimeout(10, TimeUnit.SECONDS)
				.register(JsonBindingFeature.class)
				.build();

		/*
		 * CODIGO PARA O TESTE LOCAL
		 */
		String uriRedirect = String.format("http://%s:%s/api", "localhost", 8080);
		apiRedirect = client.target(uriRedirect);
		
		String uriShorten = String.format("http://%s:%s/api/urlRepository", "localhost", 8080);
		apiShorten = client.target(uriShorten);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void shortenUrlNoCustomAlias() {

		//
		// Shorten URL: Chamada sem CUSTOM_ALIAS
		//
		String longUrl = "http://www.jcb.com.br";
		UrlRepo urlRepo = new UrlRepo();
		urlRepo.setLongUrl(longUrl);

		Response response = apiShorten.request(MediaType.APPLICATION_JSON).post(Entity.json(urlRepo));

		UrlRepo urlRepoAlreadyCreated = response.readEntity(UrlRepo.class);

		assertThat(urlRepoAlreadyCreated, is(notNullValue()));
		assertThat(urlRepoAlreadyCreated.getLongUrl(), is(equalTo(longUrl)));
		assert response.getStatus() == 201;
	}

	@Test
	public void shortenUrlWithCustomAlias() {

		//
		// Shorten URL: Chamada com CUSTOM_ALIAS
		//
		UrlRepo urlRepo = new UrlRepo();
		RandomStringTest gen = new RandomStringTest(8, ThreadLocalRandom.current());
		String customAlias = gen.nextString();
		urlRepo.setAlias(customAlias);
		urlRepo.setLongUrl("https://docs.huihoo.com/jersey/2.13/monitoring_tracing.html");

		Response response = apiShorten.request(MediaType.APPLICATION_JSON).post(Entity.json(urlRepo));

		assert response.getStatus() == 201;
	}

	@Test
	public void shortenUrlExistentCustomAlias() {

		//
		// Shorten URL: Chamada com CUSTOM_ALIAS que já existe
		//

		UrlRepo urlRepo = new UrlRepo();
		// Custom alias reutilizado de "Shorten URL: Chamada com CUSTOM_ALIAS"

		urlRepo.setAlias(createdCustomAlias);
		urlRepo.setLongUrl("https://codahale.com/what-makes-jersey-interesting-injection-providers/");
		Response response = apiShorten.request(MediaType.APPLICATION_JSON).post(Entity.json(urlRepo));
		//assert response.getStatus() == 409;

	}

	@Test
	public void redirectCustomAliasNotFound() {

		//
		// Retrieve URL: SHORTENED URL NOT FOUND
		//

		Response response = apiRedirect.path("/urlRepository").path("/{alias}").resolveTemplate("alias", "2_1_2").request()
				.accept(MediaType.APPLICATION_JSON).get();

		assert response.getStatus() == 404;

	}

	@Test
	public void redirect() {

		//
		// Retrieve URL: REDIRECT
		//

		String aliasAlreadyCreated = createdCustomAlias;
		Response response = apiRedirect.path("/urlRepository").path("/{alias}").resolveTemplate("alias", aliasAlreadyCreated)
				.request().accept(MediaType.APPLICATION_JSON).get();

		assert (response.getStatus() == 307 || response.getStatus() == 404);
		
		if (response.getStatus() == 307) {
			UrlRepo urlRepoToBeRedirected = response.readEntity(UrlRepo.class);

			assertThat(urlRepoToBeRedirected, is(notNullValue()));
		}
		

	}

	private static String basePath() {
		URL resource = UrlShortenerServiceTest.class.getResource("/");
		File file;
		try {
			file = new File(resource.toURI()).getParentFile().getParentFile();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
		return file.getAbsolutePath();
	}
	
	// FOR DEVELOPMENT ONLY
	
	/*
	 * private static GenericType<List<UrlRepo>> urlRepoList() { return new
	 * GenericType<List<UrlRepo>>() { }; }
	 */
}

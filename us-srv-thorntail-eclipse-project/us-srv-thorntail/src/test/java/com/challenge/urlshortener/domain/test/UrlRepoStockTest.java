package com.challenge.urlshortener.domain.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.challenge.urlshortener.domain.UrlRepo;
import com.challenge.urlshortener.domain.UrlRepoStock;
import com.challenge.urlshortener.util.test.RandomStringTest;

/**
 * JPA unit test for the UrlRepoStock.
 */
public class UrlRepoStockTest {
	
	private UrlRepoStock urlRepoStock;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory("urlRepos-test");
		entityManager = entityManagerFactory.createEntityManager();
		urlRepoStock = new UrlRepoStock();
		// DO NOT USE A CONSTRUCTOR HERE TO SET ENTITYMANAGER AND LOGGER
		urlRepoStock.setEntityManager(entityManager);
		urlRepoStock.setLogger(Logger.getAnonymousLogger());
	}

	@After
	public void tearDown() throws Exception {
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void createUrlRepo() {
		
		RandomStringTest gen = new RandomStringTest(8, ThreadLocalRandom.current());
		String alias = gen.nextString();
		UrlRepo urlRepo = new UrlRepo("http://www.raialeve.com.br", "http://localhost:8080/api/urlRepository/" + alias , alias);

		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		urlRepoStock.create(urlRepo);
		t.commit();

		UrlRepo created = urlRepoStock.findByAlias(urlRepo.getAlias());
		assertThat(urlRepo, equalTo(created));
		
		// Avoid duplicates when the jUnit test run several times
		Collection<UrlRepo> urlRepos = urlRepoStock.findAll();
		urlRepos.forEach(item -> {
			UrlRepo urlRepoFromCollection = (UrlRepo) item;
			String id = urlRepoFromCollection.getId();
			if ("http://www.raialeve.com.br".equals(urlRepoFromCollection.getLongUrl())) {
				urlRepoStock.delete(id);
			}
			
		});
		
	}

	@Test
	public void findByAlias() {
		Collection<UrlRepo> urlRepos = urlRepoStock.findAll();
		urlRepos.forEach(item -> {
			assertThat(urlRepoStock.findByAlias(((UrlRepo) item).getAlias()), is(notNullValue()));
		});

	}
	
}
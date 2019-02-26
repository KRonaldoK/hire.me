package com.challenge.urlshortener.domain.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.challenge.urlshortener.domain.Sequence;
import com.challenge.urlshortener.domain.SequenceStock;

/**
 * JPA unit test for the UrlRepoStock.
 */
public class SequenceStockTest {

	private SequenceStock sequenceStock;
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
		entityManagerFactory = Persistence.createEntityManagerFactory("urlRepos-test");
		entityManager = entityManagerFactory.createEntityManager();
		sequenceStock = new SequenceStock();
		// DO NOT USE A CONSTRUCTOR HERE TO SET ENTITYMANAGER AND LOGGER
		sequenceStock.setEntityManager(entityManager);
		sequenceStock.setLogger(Logger.getAnonymousLogger());
	}

	@After
	public void tearDown() throws Exception {
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void createInitialSequence() {

		Sequence sequence;
		try {
			sequence = sequenceStock.findUrlRepoSeqByName();
			if (sequence==null) {
				Sequence newSequence = new Sequence("url_repo_id", new Long(0L));

				EntityTransaction t = entityManager.getTransaction();
				t.begin();
				sequenceStock.create(newSequence);
				t.commit();

				Sequence created = sequenceStock.findUrlRepoSeqByName();
				assertThat(newSequence, equalTo(created));
			} else {
				assertThat(sequence.getCollectionNameId(), is("url_repo_id"));
			}
			
		} catch (NoResultException e) {
			
		}

	}

}
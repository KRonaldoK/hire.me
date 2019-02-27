package com.challenge.urlshortener.domain;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * The UrlRepoStock implementation is used to find and persist the pair (alias
 * url / long url).
 */
@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SequenceStock {

	@PersistenceContext(unitName = "urlRepos")
	private EntityManager entityManager;
	@Inject
	private Logger logger;

	public Sequence findUrlRepoSeqByName() {
		logger.log(Level.INFO, "Will try to find the UrlRepo sequence.");

		Sequence sequence = null;
		try {
			TypedQuery<Sequence> findUrlRepoSeqByName = entityManager
					.createNamedQuery(Sequence.FIND_URL_REPO_SEQ_BY_NAME, Sequence.class);

			sequence = findUrlRepoSeqByName.getSingleResult();

		} catch (NoResultException e) {
			logger.log(Level.INFO, "Did not found urlRepo sequence");
			sequence = null;
		}

		return sequence;
	}

	public void create(Sequence sequence) {

		logger.log(Level.INFO, "Creating {0}.", sequence);

		entityManager.persist(sequence);

	}

	public void update(Sequence sequence) {
		Objects.requireNonNull(sequence);
		logger.log(Level.INFO, "Updating sequence {0}.", new Object[] { sequence });
		entityManager.merge(sequence);
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public SequenceStock() {
		super();
	}

}

package com.challenge.urlshortener.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

import com.challenge.urlshortener.integration.UrlRepoMissingAttributeException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * The UrlRepoStock implementation is used to find and persist the pair (alias
 * url / long url).
 */
@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UrlRepoStock {

	@PersistenceContext(unitName = "urlRepos")
	private EntityManager entityManager;
	@Inject
	private Logger logger;

	public UrlRepo findByAlias(String alias) {
		logger.log(Level.INFO, "Will try to find urlRepo with alias [{0}].", alias);

		UrlRepo urlRepo = null;
		try {
			TypedQuery<UrlRepo> findByAlias = entityManager.createNamedQuery(UrlRepo.FIND_BY_ALIAS, UrlRepo.class);
			findByAlias.setParameter("anAlias", Objects.requireNonNull(alias)).getSingleResult();

			urlRepo = findByAlias.getSingleResult();
			if (urlRepo == null) {
				logger.log(Level.INFO, "Did not found urlRepo with ALIAS [{0}]", alias);
			}
		} catch (NoResultException e) {
			// e.printStackTrace();
			logger.log(Level.INFO, "Did not found urlRepo with ALIAS [{0}]", alias);
			urlRepo = null;
		}

		return urlRepo;
	}

	public boolean exists(String alias) {
		logger.log(Level.INFO, "Find urlRepo with ALIAS {0}.", alias);

		TypedQuery<UrlRepo> findByAlias = entityManager.createNamedQuery(UrlRepo.FIND_BY_ALIAS, UrlRepo.class);
		try {
			findByAlias.setParameter("anAlias", Objects.requireNonNull(alias)).getSingleResult();
		} catch (NoResultException e) {

			return false;
		}

		return true;
	}

	public void create(UrlRepo urlRepo) {
		
		logger.log(Level.INFO, "Creating {0}.", urlRepo);

		entityManager.persist(urlRepo);
		
	}

	public void delete(String id) {
		Objects.requireNonNull(id);
		logger.log(Level.INFO, "Deleting urlRepo with ID {0}.", id);
		UrlRepo reference = entityManager.getReference(UrlRepo.class, id);
		entityManager.remove(reference);
	}

	public Collection<UrlRepo> findAll() {
		logger.log(Level.INFO, "Find all urlRepos.");
		TypedQuery<UrlRepo> findAll = entityManager.createNamedQuery(UrlRepo.FIND_ALL, UrlRepo.class);
		return Collections.unmodifiableCollection(findAll.getResultList());
	}

	public UrlRepo findById(String id) {
		logger.log(Level.INFO, "Find planet with id [{0}].", id);

		UrlRepo urlRepo = null;
		try {
			TypedQuery<UrlRepo> findJustOne = entityManager.createNamedQuery(UrlRepo.FIND_JUST_ONE, UrlRepo.class);
			findJustOne.setParameter("anId", Objects.requireNonNull(id)).getSingleResult();

			List<UrlRepo> urlRepos = findJustOne.getResultList();
			if (urlRepos == null) {
				logger.log(Level.INFO, "Did not found urlRepo with ID [{0}]", id);
			} else {
				urlRepo = urlRepos.get(0);
			}
		} catch (RuntimeException e) {
			urlRepo = null;
		}

		return urlRepo;
	}
	
	public UrlRepo findByLongUrl(String longUrl) {
		logger.log(Level.INFO, "Will try to find urlRepo with long url [{0}].", longUrl);
		
		UrlRepo urlRepo = null;
		try {
			TypedQuery<UrlRepo> findByLongUrl = entityManager.createNamedQuery(UrlRepo.FIND_BY_LONG_URL, UrlRepo.class);
			findByLongUrl.setParameter("aLongUrl", Objects.requireNonNull(longUrl)).getSingleResult();

			urlRepo = findByLongUrl.getSingleResult();
			if (urlRepo == null) {
				logger.log(Level.INFO, "Did not found urlRepo with long url [{0}]", longUrl);
			}
		} catch (NoResultException e) {
			
			logger.log(Level.INFO, "Did not found urlRepo with long url [{0}]", longUrl);
			urlRepo = null;
		}

		return urlRepo;
	}
	
	public UrlRepo findByShortUrl(String shortUrl) {
		logger.log(Level.INFO, "Will try to find urlRepo with short url [{0}].", shortUrl);
		
		UrlRepo urlRepo = null;
		try {
			TypedQuery<UrlRepo> findByShortUrl = entityManager.createNamedQuery(UrlRepo.FIND_BY_SHORT_URL, UrlRepo.class);
			findByShortUrl.setParameter("aShortUrl", Objects.requireNonNull(shortUrl)).getSingleResult();

			urlRepo = findByShortUrl.getSingleResult();
			if (urlRepo == null) {
				logger.log(Level.INFO, "Did not found urlRepo with short url [{0}]", shortUrl);
			}
		} catch (NoResultException e) {
			
			logger.log(Level.INFO, "Did not found urlRepo with short url [{0}]", shortUrl);
			urlRepo = null;
		}

		return urlRepo;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public UrlRepoStock() {
		super();
	}

}

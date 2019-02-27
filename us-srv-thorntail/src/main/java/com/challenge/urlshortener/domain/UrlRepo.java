package com.challenge.urlshortener.domain;

import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.ws.rs.Path;

import org.hibernate.annotations.GenericGenerator;


/**
 * Ulr repository entity.
 */
@Entity
@Table(name = "UrlRepo")
@NamedQuery(name = UrlRepo.FIND_ALL, query = "FROM UrlRepo p") 
@NamedQuery(name = UrlRepo.FIND_JUST_ONE, query = "FROM UrlRepo p WHERE p.id = :anId")
@NamedQuery(name = UrlRepo.FIND_BY_ALIAS, query = "FROM UrlRepo p WHERE p.alias = :anAlias")
@NamedQuery(name = UrlRepo.FIND_BY_LONG_URL, query = "FROM UrlRepo p WHERE p.longUrl = :aLongUrl")
@NamedQuery(name = UrlRepo.FIND_BY_SHORT_URL, query = "FROM UrlRepo p WHERE p.shortUrl = :aShortUrl")
@JsonbPropertyOrder(value = { "alias", "shortUrl", "longUrl", "statistics" })
public class UrlRepo {

	@Id
	@GeneratedValue(generator = "uuid") // mongodb: Alterar preferencias em JPA, erros and warnings
	@GenericGenerator(name = "uuid", strategy = "uuid2") // mongodb
	@JsonbTransient
	private String id;

	@Column(name = "long_url", nullable = false, unique = true)
	private String longUrl;

	@Column(name = "short_url", nullable = false, unique = true)
	private String shortUrl;

	@Column(name = "alias", nullable = false, unique = true)
	private String alias;

	@JsonbTransient
	@Column(name = "id_key", nullable = true, unique = true)
	private Long idKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getIdKey() {
		return idKey;
	}

	public void setIdKey(Long idKey) {
		this.idKey = idKey;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public UrlRepo() {
		super();
	}

	@Transient
	private Statistics statistics;

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public void computeStatistics() throws Exception {
		// TODO:
		// this.aparicoes = new Integer(getFilmes().size());
	}

	public UrlRepo(String longUrl, String shortUrl, String alias) {
		super();
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
		this.alias = alias;
		this.idKey = idKey;

	}

	@Override
	public String toString() {
		return "UrlRepo{id=[" + id + "] , longUrl=[" + longUrl + "], shortUrl=[" + shortUrl + "], idKey=[" + idKey
				+ "], alias=[" + alias +"]}";
	}

	// is it necessary?
	@Override
	public boolean equals(Object anObject) {
		if (this == anObject) {
			return true;
		}
		if (anObject instanceof UrlRepo) {
			UrlRepo urlRepo = (UrlRepo) anObject;
			if (alias.equals(urlRepo.getAlias())) {
				return true;
			}
		}
		return false;
	}

	static final String FIND_ALL = "UrlRepo.findAll";

	static final String FIND_JUST_ONE = "UrlRepo.findJustOne";

	static final String FIND_BY_ALIAS = "UrlRepo.findByAlias";
	
	static final String FIND_BY_LONG_URL = "UrlRepo.findByLongUrl";
	
	static final String FIND_BY_SHORT_URL = "UrlRepo.findByShortUrl";

}

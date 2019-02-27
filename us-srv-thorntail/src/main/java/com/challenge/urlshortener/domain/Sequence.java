package com.challenge.urlshortener.domain;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Sequence entity.
 */
@Entity
@Table(name = "Sequence")
@NamedQuery(name = Sequence.FIND_URL_REPO_SEQ_BY_NAME, query = "FROM Sequence s")
public class Sequence {

	@Id
	@GeneratedValue(generator = "uuid") // mongodb: Alterar preferencias em JPA, erros and warnings
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@JsonbTransient
	private String id;

	@Column(name = "collection_name_id", nullable = false, unique = true)
	private String collectionNameId;

	@Column(name = "seq", nullable = false, unique = false)
	private Long seq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public String getCollectionNameId() {
		return collectionNameId;
	}

	public void setCollectionNameId(String collectionNameId) {
		this.collectionNameId = collectionNameId;
	}

	public Sequence() {
		super();
	}

	public Sequence(String collectionNameId, Long seq) {
		super();
		this.collectionNameId = collectionNameId;
		this.seq = seq;
	}

	static final String FIND_URL_REPO_SEQ_BY_NAME = "Sequence.findUrlRepoSeqByName";

}

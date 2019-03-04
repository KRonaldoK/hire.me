package com.challenge.urlshortener.boundary;

import javax.enterprise.context.RequestScoped;

import com.challenge.urlshortener.domain.UrlRepo;


@RequestScoped
public abstract class ShortenUrlCommand {
	
	public abstract UrlRepo execute() throws Exception;
	public ShortenUrlCommand() {
		super();
	};
}

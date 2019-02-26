package com.challenge.urlshortener.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.challenge.urlshortener.domain.Statistics;
import com.challenge.urlshortener.domain.UrlRepo;

/**
 * Simple Sub Resource for the statistics of a urlRepo.
 */
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {
    private final UrlRepo urlrepo;

    StatisticsResource(UrlRepo urlrepo) {
        this.urlrepo = urlrepo;
    }
    
    @GET
    public Statistics get() throws Exception {
        return urlrepo.getStatistics();
    }
}

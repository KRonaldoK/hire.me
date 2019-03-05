package com.challenge.urlshortener.integration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * An {@link ExceptionMapper} implementation for all Swapi related {@link StarWarsException}s.
 */
@Provider
public class UrlShortenerExceptionMapper implements ExceptionMapper<UrlShortenerException> {
    @Override
    public Response toResponse(UrlShortenerException exception) {
        if (exception instanceof UrlRepoAliasWasFoundException) {
        	
            Map<String, String> response = new LinkedHashMap<>();
            response.put("alias", ((UrlRepoAliasWasFoundException) exception).getUrlRepo().getAlias());
            response.put("code", "ERR-0001");
            response.put("type", "USHORT-SRV");
            response.put("message", exception.getMessage());

            return Response.status(Status.CONFLICT)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();

        } else if (exception instanceof UrlRepoMissingShortUrlException) {
        	
        	Map<String, String> response = new HashMap<>();
            response.put("code", "ERR-0002");
            response.put("type", "USHORT-SRV");
            response.put("message", exception.getMessage());

            return Response.status(Status.NOT_FOUND)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();
            
        } else if (exception instanceof UrlRepoMissingAttributeException) {
        	
        	Map<String, String> response = new HashMap<>();
            response.put("code", "ERR-0003");
            response.put("type", "USHORT-SRV");
            response.put("message", exception.getMessage());

            return Response.status(Status.BAD_REQUEST)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();
            
        } else if (exception instanceof InvalidLongUrlException) {
        	
        	Map<String, String> response = new HashMap<>();
            response.put("code", "ERR-0004");
            response.put("type", "USHORT-SRV");
            response.put("message", exception.getMessage());

            return Response.status(Status.BAD_REQUEST)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();
            
        }  else if (exception instanceof MongodbSequenceNotFoundException) {
        	
        	Map<String, String> response = new HashMap<>();
            response.put("code", "ERR-0005");
            response.put("type", "USHORT-SRV");
            response.put("message", exception.getMessage());

            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();
            
        }else {
        	
            Map<String, String> response = new HashMap<>();
            response.put("code", "ERR-0000");
            response.put("type", "SWAPI");
            response.put("message", exception.getMessage());

            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity(response).type(MediaType.APPLICATION_JSON).build();
            
        }
        
        
        
    }
}

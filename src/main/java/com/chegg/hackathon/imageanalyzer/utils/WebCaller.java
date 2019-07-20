package com.chegg.hackathon.imageanalyzer.utils;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


public class WebCaller {
	
	private static final String SUBSCRIPTION_KEY = "45d0153241fb43369beb3934ee90312f";
	
	private static Logger logger = LoggerFactory.getLogger(WebCaller.class);
	
	public Object doPost(String url, String body,
			Map<String, String> queryParams, Map<String,String> headerValues) {
	    
		  HttpHeaders headers = new HttpHeaders();
		  
		  
		  for(Entry<String, String> entrySet : headerValues.entrySet()) {
			  headers.set(entrySet.getKey(), entrySet.getValue());
		  }
		  
//		  headers.set("Content-Type", MediaType.TEXT_PLAIN_VALUE);
//		  headers.set("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);
//		  headers.set("Postman-Token", "e9fddf59-da7b-4218-8f39-2eb9c70597c7");
//		  headers.set("cache-control", "no-cache");
		  
		  
		  HttpEntity<String> request = new HttpEntity<String>(
		            body, headers);
		  
		  UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		  
		  if (queryParams != null && !queryParams.isEmpty()) {
		    for (Entry<String, String> param : queryParams.entrySet()) {
		      builder.queryParam(param.getKey(), param.getValue());
		    }
		  }
		  
		  RestTemplate restTemplate = new RestTemplate();
		  
		  try {
			return restTemplate.postForObject(builder.build().encode().toUri(), request ,Object.class);
		  } catch (HttpClientErrorException exception) {
			  if (exception.getStatusCode().value() != HttpStatus.SC_NOT_FOUND) {
				  throw exception;
			  } else {
				  return null;
			  }
		   }
	}
	
	
	public Object doGet(String url, Map<String, String> queryParams) {

	    if (url == null || url.length() ==0) {
	      logger.warn("Inavalid URL" + url);
	    }

	    logger.info("Calling GET " + url);

	    if (queryParams != null && !queryParams.isEmpty()) {
	      logger.info("with queryParams " + queryParams);
	    }

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

	    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

	    if (queryParams != null && !queryParams.isEmpty()) {
	      for (Entry<String, String> param : queryParams.entrySet()) {
	        builder.queryParam(param.getKey(), param.getValue());
	      }
	    }

	    RestTemplate restTemplate = new RestTemplate();

	    try {
	      return restTemplate.getForObject(builder.build().encode().toUri(), Object.class);
	    } catch (HttpClientErrorException exception) {
	      if (exception.getStatusCode().value() != HttpStatus.SC_NOT_FOUND) {
	        throw exception;
	      } else {
	        return null;
	      }
	    }

	  }
	
}

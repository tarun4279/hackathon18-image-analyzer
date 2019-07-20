package com.chegg.hackathon.imageanalyzer.configs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chegg.hackathon.imageanalyzer.service.DataService;
import com.chegg.hackathon.imageanalyzer.service.GoogleVisionService;
import com.chegg.hackathon.imageanalyzer.service.MongoService;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public GoogleVisionService googleVisionService(@Value("${google.credentials.file.path}")
	String credsFilePath) throws FileNotFoundException, IOException {
		
		return new GoogleVisionService(credsFilePath);
	}
	
	@Bean
	public DataService dataService(@Value("${elasticsearch.base.url}") String esBaseUrl) {
		return new DataService(esBaseUrl);
	}
	
	@Bean
	public MongoService getMongoService() {
		return new MongoService();
	}
}

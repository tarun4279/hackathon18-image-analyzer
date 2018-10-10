package com.chegg.hackathon.imageanalyzer.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chegg.hackathon.imageanalyzer.service.DataService;
import com.chegg.hackathon.imageanalyzer.service.ImageToTextService;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public ImageToTextService imageToTextService() {
		return new ImageToTextService("test-service");
	}
	
	@Bean
	public DataService dataService(@Value("${elasticsearch.base.url}") String esBaseUrl) {
		return new DataService(esBaseUrl);
	}
}

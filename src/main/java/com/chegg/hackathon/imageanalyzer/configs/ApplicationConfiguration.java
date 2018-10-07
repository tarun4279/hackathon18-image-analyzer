package com.chegg.hackathon.imageanalyzer.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chegg.hackathon.imageanalyzer.service.ImageToTextService;

@Configuration
public class ApplicationConfiguration {

	@Bean
	public ImageToTextService imageToTextService() {
		return new ImageToTextService("test-service");
	}
	
	
}

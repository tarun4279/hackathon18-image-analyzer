package com.chegg.hackathon.imageanalyzer.service;

import org.springframework.stereotype.Service;

@Service
public class ImageToTextService {

	private String serviceName;
	
	public ImageToTextService(String name) {
		this.serviceName = name;
	}
	
	
	public String getServiceName() {
		return this.serviceName;
	}
	
}

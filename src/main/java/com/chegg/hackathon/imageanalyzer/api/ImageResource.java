package com.chegg.hackathon.imageanalyzer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chegg.hackathon.imageanalyzer.service.DataService;
import com.chegg.hackathon.imageanalyzer.service.ImageToTextService;

@RestController
@RequestMapping("/rest/image")
public class ImageResource {

	@Autowired
	private ImageToTextService imageToTextService;
	
	@Autowired
	private DataService dataService;
	
	@RequestMapping("/serviceList")
	public String checkService() {
		return imageToTextService.getServiceName() + " - " + dataService.getName();
	}
	
}

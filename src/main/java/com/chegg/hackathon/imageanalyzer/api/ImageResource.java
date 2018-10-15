package com.chegg.hackathon.imageanalyzer.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chegg.hackathon.imageanalyzer.handlers.lambda.MediaImageAddedHandler;
import com.chegg.hackathon.imageanalyzer.models.GenericResponse;
import com.chegg.hackathon.imageanalyzer.models.ImageEntity;
import com.chegg.hackathon.imageanalyzer.service.DataService;

@RestController
@RequestMapping("/rest/image")
public class ImageResource {


	@Autowired
	private DataService dataService;
	
	@RequestMapping("/serviceList")
	public String checkService() {
		return  dataService.getName();
	}
	
	@RequestMapping("/upload")
	public String checkImageUpload() throws FileNotFoundException, IOException {
		
		List<String> cheggTags = new ArrayList<>();
		cheggTags.add("tag1");
		cheggTags.add("tag2");
		
		ImageEntity imageEntity = new ImageEntity();
		imageEntity.setCheggSource("question");
		imageEntity.setCheggSubjectId(3);
		imageEntity.setImageUrl("https://i.stack.imgur.com/vrkIj.png");
		imageEntity.setCheggTags(cheggTags);
		
		MediaImageAddedHandler handler = new MediaImageAddedHandler();
		
		GenericResponse<String> response =  handler.handleRequest(imageEntity, null);
		
		return response.getResponse();
	}
	
}

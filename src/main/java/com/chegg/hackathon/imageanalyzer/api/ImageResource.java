package com.chegg.hackathon.imageanalyzer.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	
	public MediaImageAddedHandler handler;
	
	public ImageResource() throws FileNotFoundException, IOException {
		handler = new MediaImageAddedHandler();
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
		imageEntity.setIndexName("images");
		
		
		
		GenericResponse<String> response =  handler.handleRequest(imageEntity, null);
		
		return response.getResponse();
	}
	
	
	@RequestMapping(value = "/uploadImage/{index}", method = RequestMethod.POST)
	public String uploadImageToIndex(@RequestBody ImageEntity entity, @PathVariable String index) {
		
		System.out.println("Uploading " + entity.toString() + " to " + index);
		entity.setIndexName(index);
		
		GenericResponse<String> response =  handler.handleRequest(entity, null);
		
		return response.getResponse();
	}
	
	
	@RequestMapping(value = "/uploadImageData/{index}", method = RequestMethod.POST)
	public String uploadImage(@RequestParam("file") MultipartFile file, @PathVariable String index) {
		
		System.out.println("Uploading file to " + index);
		ImageEntity entity = new ImageEntity();
		entity.setIndexName(index);
		entity.setImageData(file);
		
		GenericResponse<String> response =  handler.handleRequest(entity, null);
		
		return response.getResponse();
	}
	
	
	
	@RequestMapping(value = "/getImages", method = RequestMethod.GET)
	@ResponseBody
	public List<ImageEntity> getImageEntities(@RequestParam("query") String query, @RequestParam("index") String index) throws IOException {
		
		System.out.println("Got query for " + query + " for index " + index);
		
		List<String> queryFields = new ArrayList<String>();
		queryFields.add("imageTranscriptionData");
		
		List<ImageEntity> output = this.dataService.searchIndexWithField(query, index, queryFields);
		
		return output;
	}
	
	
}

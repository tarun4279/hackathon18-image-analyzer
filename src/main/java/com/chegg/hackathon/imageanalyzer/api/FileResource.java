package com.chegg.hackathon.imageanalyzer.api;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chegg.hackathon.imageanalyzer.models.GenericResponse;
import com.chegg.hackathon.imageanalyzer.service.FileService;

@RestController
@RequestMapping("/rest/file")
public class FileResource {

	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public GenericResponse<Boolean> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String fileName) {
		
		System.out.println("Uploading file " + fileName);
				
		return fileService.save(file, fileName) ;
		
	}
	
	@RequestMapping(value = "/{fileName}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getFile(@PathVariable String fileName) throws URISyntaxException, IOException {

		File file = fileService.get(fileName).getResponse();
		
		Path path = Paths.get(file.getAbsolutePath());
	    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

	    return ResponseEntity.ok()
	            .contentLength(file.length())
	            .body(resource);
		
	}
	
}

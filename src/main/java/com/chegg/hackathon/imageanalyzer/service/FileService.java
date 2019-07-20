package com.chegg.hackathon.imageanalyzer.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.chegg.hackathon.imageanalyzer.models.GenericResponse;

@Service
public class FileService {
	
	private static final String APPLICATION_FILE_STORAGE_DIR = System.getProperty("user.home").concat("/files/");
	

	public GenericResponse<Boolean> save(MultipartFile file, String fileName) {
		
		GenericResponse<Boolean> response = new GenericResponse<>() ;

		
		try {
			file.transferTo(new File(APPLICATION_FILE_STORAGE_DIR.concat(fileName)));
			return response.setResponse(true);
		} catch (IllegalStateException | IOException e) {
			System.err.println("Unable to upload file due to " + e.getMessage());
			return response.setResponse(false).setErrorDetails(e.getMessage(), 0);

		}
	}
	
	
	public GenericResponse<File> get(String fileName) {
		
		GenericResponse<File> response = new GenericResponse<>() ;
		
		if(Files.isReadable(Paths.get(APPLICATION_FILE_STORAGE_DIR.concat(fileName)))) {
			File out = new File(APPLICATION_FILE_STORAGE_DIR.concat(fileName));
			
			return response.setResponse(out);
		}else {
			return response.setErrorDetails("File not found", 0);
		}
		
		
	}
	
}

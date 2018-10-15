package com.chegg.hackathon.imageanalyzer.handlers.lambda;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chegg.hackathon.imageanalyzer.models.GenericResponse;
import com.chegg.hackathon.imageanalyzer.models.ImageEntity;
import com.chegg.hackathon.imageanalyzer.models.VisionResponseEntity;
import com.chegg.hackathon.imageanalyzer.service.DataService;
import com.chegg.hackathon.imageanalyzer.service.GoogleVisionService;

import io.searchbox.core.DocumentResult;


public class MediaImageAddedHandler implements RequestHandler<ImageEntity, GenericResponse<String>>{

	private static final Logger LOGGER = LoggerFactory.getLogger(MediaImageAddedHandler.class);
	
	private GoogleVisionService visionClient;
	
	private DataService dataServiceClient;
	
	
	public MediaImageAddedHandler() throws FileNotFoundException, IOException {
		
		this.visionClient = new GoogleVisionService("src/main/resources/creds.json");
		
		this.dataServiceClient = new DataService("http://4ec6b2cc.ngrok.io/");
		
	}
	
	
	@Override
	public GenericResponse<String> handleRequest(ImageEntity imageEntity, Context context) {
		// TODO Auto-generated method stub
		
		GenericResponse<String> handlerResponse = new GenericResponse<>();
		
		LOGGER.info("Received an Image with details : "+ imageEntity.toString());
		
		LOGGER.info("Sending Image to Cloud Vision API : "+ imageEntity.getImageUrl());
		
		GenericResponse<VisionResponseEntity> visionResponse = new GenericResponse<>();
		
		try {
			visionResponse = this.visionClient.transcribeImage(imageEntity.getImageUrl());
			
			if(visionResponse.getErrorCode() != null) {
				
				VisionResponseEntity visionTranscriptionInfo = visionResponse.getResponse();
				
				LOGGER.info("Got transciption results for image " + 
								imageEntity.getImageUrl() + 
								" as " + visionTranscriptionInfo.getTranscribedData() +
								" with score " + visionTranscriptionInfo.getScore());
				
				imageEntity.setImageTranscriptionData(visionTranscriptionInfo.getTranscribedData());
				imageEntity.setImageTranscriptionScore(visionTranscriptionInfo.getScore());
				
				
				LOGGER.info("Saving the image" + imageEntity.getImageUrl() + " with "
						+ "transcription results to Elastic Storage");
				
				GenericResponse<DocumentResult> storageResult = dataServiceClient.storeData(imageEntity,
						imageEntity.getIndexName(), "transcriptionData");
				
				
				if(storageResult.getErrorCode() != null) {
					handlerResponse.setErrorDetails(storageResult.getErrorMessage(), storageResult.getErrorCode());
				} else {
					handlerResponse.setResponse("Image Upload Successfull");				
				}
				
				
			} else {
				handlerResponse.setErrorDetails(visionResponse.getErrorMessage(), visionResponse.getErrorCode());
			}
			
			
			
		} catch (IOException e) {
			
			LOGGER.error("Unable to process the image " + imageEntity.getImageUrl() + " due to " + e.getMessage());
			
			handlerResponse.setErrorDetails("Unable to process the image due to " + e.getMessage(), -1);
			
		}
		
		return handlerResponse;
	}

}

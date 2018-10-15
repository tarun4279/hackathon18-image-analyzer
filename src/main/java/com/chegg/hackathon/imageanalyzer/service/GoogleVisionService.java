package com.chegg.hackathon.imageanalyzer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chegg.hackathon.imageanalyzer.models.GenericResponse;
import com.chegg.hackathon.imageanalyzer.models.VisionResponseEntity;
import com.chegg.hackathon.imageanalyzer.utils.ImageUtils;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;

@Service
public class GoogleVisionService {

	
	private ImageAnnotatorClient visionClient;
	
	
	public GoogleVisionService(String credentialsPath) throws FileNotFoundException, IOException {
		
		GoogleCredentials credentials = GoogleCredentials.fromStream
				(new FileInputStream((new File(credentialsPath))));
		
		
		ImageAnnotatorSettings imageAnnotatorSettings =
				      ImageAnnotatorSettings.newBuilder()
				          .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
				          .build();
		
		
		visionClient = ImageAnnotatorClient.create(imageAnnotatorSettings);
		
	}
	
	public GenericResponse<VisionResponseEntity> transcribeImage(String remoteImageUrl) throws IOException{
		
		ByteString imgBytes = ImageUtils.convertRemoteImageToByteString(remoteImageUrl);
		
		if(imgBytes == null) {
			return new GenericResponse<VisionResponseEntity>().setErrorDetails("Unable to fetch image", -1);
		}
		
		AnnotateImageRequest request = createOCRImageRequest(imgBytes);
		
		return transcribeImage(request);
	}
	
	
	
	private AnnotateImageRequest createOCRImageRequest(ByteString imgBytes) {
		
		Image img = Image.newBuilder().setContent(imgBytes).build();
	    
		Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).setMaxResults(1).build();
	    
	    return AnnotateImageRequest.newBuilder()
	          .addFeatures(feat)
	          .setImage(img)
	          .build();
	    
	}
	
	
	public GenericResponse<VisionResponseEntity> transcribeImage(AnnotateImageRequest imageRequest) {
		
		List<AnnotateImageRequest> requests = new ArrayList<>();
		requests.add(imageRequest);
		
		List<AnnotateImageResponse> responseList = this.visionClient.batchAnnotateImages(requests).getResponsesList();
		
		AnnotateImageResponse response = responseList.get(0);
		
		GenericResponse<VisionResponseEntity> genericResponse = new GenericResponse<VisionResponseEntity>();
		
		if(response.getError() != null) {
			genericResponse.setErrorDetails(
					response.getError().getMessage(), response.getError().getCode());
		}
		
		if(response.getTextAnnotationsList() != null) {
			EntityAnnotation data = response.getTextAnnotationsList().get(0);
			
			
			VisionResponseEntity visionResponseEntity = 
					new VisionResponseEntity(data.getDescription(), data.getConfidence());
			
			
			genericResponse.setResponse(visionResponseEntity);
		}
		
		return genericResponse;
	}
	
	
}

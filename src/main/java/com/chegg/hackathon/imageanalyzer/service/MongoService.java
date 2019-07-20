package com.chegg.hackathon.imageanalyzer.service;

import java.util.List;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.chegg.hackathon.imageanalyzer.models.Topic;
import com.chegg.hackathon.imageanalyzer.utils.WebCaller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Service
public class MongoService {

	MongoClient mongoClient = new MongoClient( "mongo-db02.test3.cloud.cheggnet.com" );

	MongoDatabase futurama = mongoClient.getDatabase("futurama");

	MongoCollection<Document> collection = futurama.getCollection("topic");

	public Topic getTopicBySearchableName(String searchQuery) {
		
		Document searchDocument = new Document();
		searchDocument.append("searchableName", searchQuery);
		searchDocument.append("active", true);		
		
		Document output = this.collection.find(Filters.regex("searchableName", searchQuery)).first();

		if(output == null) {
			output =  this.collection.find(new Document().append("subjectId", 3)).first();
		}
		
		Object topicObject = new WebCaller().doGet("http://board.test3.cheggnet.com:"
				+ "6087/board-api/rest/topic/"+output.get("_id").toString(), null);
		
		ObjectMapper mapper = new ObjectMapper();

	    if(topicObject != null) {
	    	  	return mapper.convertValue(topicObject, new TypeReference<Topic>() {
	    	  	});
	    }else {
	    	return null;
	    }
	    
	}
	
}

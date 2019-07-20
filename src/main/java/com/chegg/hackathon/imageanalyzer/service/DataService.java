package com.chegg.hackathon.imageanalyzer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chegg.hackathon.imageanalyzer.models.DataResponse;
import com.chegg.hackathon.imageanalyzer.models.FederatedSearchQuestion;
import com.chegg.hackathon.imageanalyzer.models.FederatedSearchTextbook;
import com.chegg.hackathon.imageanalyzer.models.GenericResponse;
import com.chegg.hackathon.imageanalyzer.models.ImageEntity;
import com.chegg.hackathon.imageanalyzer.models.Topic;
import com.chegg.hackathon.imageanalyzer.utils.Constants;
import com.chegg.hackathon.imageanalyzer.utils.WebCaller;
import com.google.gson.Gson;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Service
public class DataService {

	
	private static final int JEST_CLIENT_READ_TIMEOUT = 10000;
	private static final int JEST_CLIENT_CONNECTION_TIMEOUT = 10000;
	
	private static final String ODIN_URL = "http://thor.prod.cheggnet.com:6033/odin-external/rest/v1/";
	
	private JestClient jestClient;
	private String baseUrl;
	
	@Autowired
	private MongoService mongoService;
	
	
	final static Logger logger = LoggerFactory.getLogger(DataService.class);
	
	
	
	
	public DataService(String baseUrl) {
	
		 logger.info("Creating a Elastic search Data Service client for " + baseUrl);
		
		 JestClientFactory jestClientFactory = new JestClientFactory();
		    jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(baseUrl)
		        .multiThreaded(true).readTimeout(JEST_CLIENT_READ_TIMEOUT)
		        .connTimeout(JEST_CLIENT_CONNECTION_TIMEOUT).build());
		 
		 this.jestClient = jestClientFactory.getObject();
		 this.baseUrl = baseUrl;
		
	}
	
	public GenericResponse<DocumentResult> storeData(Object object, String indexName, String dataType) {
		
		Index index = new Index.Builder(object).index(indexName).type(dataType).build();
		
		try {
			return new GenericResponse<DocumentResult>().setResponse(jestClient.execute(index));
		} catch (IOException e) {
			logger.error("Save failed due to for {} due to {}" , object, e.getMessage());
			return new GenericResponse<DocumentResult>().setErrorDetails("Save failed due to " + e.getMessage(), -1);
		}
		
	}
	
	public  List<ImageEntity> searchIndexWithField(String query, String index, List<String> fields) throws IOException{
		
		List<ImageEntity> output = new ArrayList<>();
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		
		// only first field search
        searchSourceBuilder
        .query(QueryBuilders.matchQuery(fields.get(0),query));		
		

		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                .addIndex(index)
		                                .addType(Constants.DEFAULT_ES_TYPE)
		                                .build();

		SearchResult result = this.jestClient.execute(search);
		
		return result.getSourceAsObjectList(ImageEntity.class);

	}
	
	
	
//	public <T> List<T> search (Entity entity, String dataType) throws IOException {
//		
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//
//		Map<String,String> queryData = entity.getEntityData();
//		
//		String[] fieldNames = (String[]) Arrays.copyOf(queryData.keySet().toArray(), queryData.keySet().size());
//		String[] values = (String[]) Arrays.copyOf(queryData.values().toArray(), queryData.values().size());
//		
//		
//		searchSourceBuilder
//        .query(QueryBuilders.multiMatchQuery(values, fieldNames));
//        
//		Search search = new Search.Builder(searchSourceBuilder.toString())
//                .addIndex(entity.getIndexName())
//                .addType(dataType)
//                .build();
// 
//		return (List<T>) this.jestClient.execute(search).getHits(entity.getClass()); 
//	}
	
	public String getName() {
		return this.baseUrl;
	}

	public DataResponse getData(String mainIntent, List<String> queries) throws JSONException, IOException {
		
		StringBuilder query = new StringBuilder();
		
		for(String q : queries) {
			query.append(q+" ");
		}
		
		switch(mainIntent) {
		
			case (Constants.QUESTION_INTENT):
				return callFederatedSearchForQuestion(query.toString());
				
			case (Constants.TBS_INTENT):
				return callFederatedSearchForTBS(query.toString());
		
			case (Constants.DEFINTION_INTENT):
				return callForDefinitionMongo(query.toString());

			case (Constants.FORMULA_INTENT):
				return callFormulaES(query.toString());
				
			
			default: 
				break;
		}
		
		return null;
	}
	
	private DataResponse callFederatedSearchForQuestion(String query) throws JSONException {
		
		/*
		 * http://thor.prod.cheggnet.com:6033/odin-external/rest/v1/
		 * search?q=what+are+the&f.profile=questions-auto&
		 * f.chgsec=nav&f.chgsubcomp=as&f.log-level=0&?f.page-number=1
		 */
		
		DataResponse dataResponse = new DataResponse();
		
		Map<String,String> queryParams = new HashMap<>();
		queryParams.put("q", query);
		queryParams.put("f.profile", "questions-auto");
		queryParams.put("f.chgsec", "nav");
		queryParams.put("f.chgsubcomp", "as");
		queryParams.put("f.log-level", "0");
		
		Object response = new WebCaller().doGet(ODIN_URL+"search", queryParams);
		
		
		Gson gson = new Gson();
		JSONObject obj = new JSONObject(gson.toJson(response)); //Make string a JSONObject
		
		if(obj.getInt("httpCode") == 200) {
			
			org.json.JSONArray resultList = obj.getJSONArray("result");
			
			JSONObject result = resultList.getJSONObject(0);
			
			JSONObject questions = result.getJSONObject("questions");
			
			JSONObject responseContent = questions.getJSONObject("responseContent");
			
			org.json.JSONArray docsArray = responseContent.getJSONArray("docs");
			
			JSONObject firstDoc = docsArray.getJSONObject(0);
			
			if(firstDoc != null) {
				FederatedSearchQuestion question = new FederatedSearchQuestion();
				question.setUrl(firstDoc.getString("url"));
				question.setUuid(firstDoc.getString("uuid"));
				
				System.out.println("Found Question Url as " + question.getUrl());
				
				dataResponse.setDataUrl("www.chegg.com"+question.getUrl());
			}else {
				System.err.println("No Question Found");
			}
			
			
		} else {
			dataResponse.setText("Couldn find it sorry");
		}
		
		return dataResponse;
	}

	
	private DataResponse callFederatedSearchForTBS(String query) throws JSONException {
		
		/*
		 * http://thor.prod.cheggnet.com:6033/odin-external/rest/v1/
		 * search?q=what+are+the&f.profile=questions-auto&
		 * f.chgsec=nav&f.chgsubcomp=as&f.log-level=0&?f.page-number=1
		 */
		
		DataResponse dataResponse = new DataResponse();
		
		Map<String,String> queryParams = new HashMap<>();
		queryParams.put("q", query);
		queryParams.put("f.profile", "federated-auto");
		queryParams.put("f.chgsec", "nav");
		queryParams.put("f.chgsubcomp", "as");
		queryParams.put("f.log-level", "0");
		
		Object response = new WebCaller().doGet(ODIN_URL+"search", queryParams);
		
		
		Gson gson = new Gson();
		JSONObject obj = new JSONObject(gson.toJson(response)); //Make string a JSONObject
		
		if(obj.getInt("httpCode") == 200) {
			
			org.json.JSONArray resultList = obj.getJSONArray("result");
			
			JSONObject result = resultList.getJSONObject(0);
			
			JSONObject questions = result.getJSONObject("textbooks");
			
			JSONObject responseContent = questions.getJSONObject("responseContent");
			
			org.json.JSONArray docsArray = responseContent.getJSONArray("docs");
			
			JSONObject firstDoc = docsArray.getJSONObject(0);
			
			if(firstDoc != null) {
				
				FederatedSearchTextbook textbook = new FederatedSearchTextbook();
				textbook.setImageUrl(firstDoc.getString("imageUrl"));
				textbook.setUrl(firstDoc.getString("url"));
				
				
				System.err.println("Found Question Url as " + textbook.getUrl());
				
				dataResponse.setDataUrl("www.chegg.com"+textbook.getUrl());
				dataResponse.setImageUrl(textbook.getImageUrl());
				
			} else {
				System.err.println("No Question Found");
			}
			
			
		} else {
			dataResponse.setText("Couldn find it sorry");
		}
		
		return dataResponse;
	}

	private DataResponse callFormulaES(String query) throws IOException {
		
		List<String> fieldsToSearch = new ArrayList<>();
		fieldsToSearch.add("imageTranscriptionData");
		
		List<ImageEntity> imageFormulas = searchIndexWithField(query,"images",fieldsToSearch);
		
		DataResponse response = new DataResponse();
		
		if(imageFormulas != null && !imageFormulas.isEmpty()) {
			response.setImageUrl(imageFormulas.get(0).getImageUrl());
			response.setTranscribedData(imageFormulas.get(0).getImageTranscriptionData());
		}
		
		return response;
	}
	
	private DataResponse callForDefinitionMongo(String query) {
		
		DataResponse dataResponse = new DataResponse();
		
		MongoService service = new MongoService();
		Topic topic = service.getTopicBySearchableName(query);
		
		if(topic != null) {
			dataResponse.setDataUrl("www.chegg.com"+topic.getOrganicUrl());
		}
		
		return dataResponse;
	}
	
}

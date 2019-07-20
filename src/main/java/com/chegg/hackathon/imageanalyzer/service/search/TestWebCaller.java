package com.chegg.hackathon.imageanalyzer.service.search;

import org.json.JSONException;

import com.chegg.hackathon.imageanalyzer.models.Topic;
import com.chegg.hackathon.imageanalyzer.service.MongoService;


public class TestWebCaller {
	
	private static final String ODIN_URL = "http://thor.prod.cheggnet.com:6033/odin-external/rest/v1/";

	public static void main(String[] args) throws JSONException {
		
		String query = "kinetics";
		
		MongoService service = new MongoService();
		Topic topic = service.getTopicBySearchableName(query);
		
		
		
		System.err.println(topic.getOrganicUrl());
		
//		Map<String,String> queryParams = new HashMap<>();
//		queryParams.put("q", query);
//		queryParams.put("f.profile", "questions-auto");
//		queryParams.put("f.chgsec", "nav");
//		queryParams.put("f.chgsubcomp", "as");
//		queryParams.put("f.log-level", "0");
//		
//		Object response = new WebCaller().doGet(ODIN_URL+"search", queryParams);
//		
//		Gson gson = new Gson();
//		JSONObject obj = new JSONObject(gson.toJson(response)); //Make string a JSONObject
//		
//		if(obj.getInt("httpCode") == 200) {
//			
//			org.json.JSONArray resultList = obj.getJSONArray("result");
//			
//			JSONObject result = resultList.getJSONObject(0);
//			
//			JSONObject questions = result.getJSONObject("questions");
//			
//			JSONObject responseContent = questions.getJSONObject("responseContent");
//			
//			org.json.JSONArray docsArray = responseContent.getJSONArray("docs");
//			
//			JSONObject firstDoc = docsArray.getJSONObject(0);
//			
//			FederatedSearchQuestion question = new FederatedSearchQuestion();
//			question.setUrl(firstDoc.getString("url"));
//			question.setUuid(firstDoc.getString("uuid"));
//			
//			System.err.println(question.getUrl());
//		}
//
//		System.out.println(response.toString().substring(0,100));

	}

}

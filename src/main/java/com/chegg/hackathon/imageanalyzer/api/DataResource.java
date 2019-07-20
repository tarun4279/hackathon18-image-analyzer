package com.chegg.hackathon.imageanalyzer.api;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chegg.hackathon.imageanalyzer.models.DataRequest;
import com.chegg.hackathon.imageanalyzer.models.DataResponse;
import com.chegg.hackathon.imageanalyzer.service.DataService;
import com.chegg.hackathon.imageanalyzer.utils.Constants;

@RestController
@RequestMapping("/rest/data")
public class DataResource {

	@Autowired
	private DataService dataService;
	
	@RequestMapping(value = "/getData", method = RequestMethod.POST)
	public DataResponse getData(@RequestBody DataRequest dataRequest) throws JSONException, IOException {
		
		String intent = dataRequest.getIntent();
		List<String> queries = dataRequest.getQueries();
		
		DataResponse response = dataService.getData(intent,queries);
		
		return response;
	}
	
	private String findMainIntent(List<String> intents, List<String> queries) {
		
		if(intents.contains(Constants.DEFINTION_INTENT)) {
			return Constants.DEFINTION_INTENT;
		}
		
		if(intents.contains(Constants.FORMULA_INTENT)) {
			return Constants.FORMULA_INTENT;
		}
		
		if(intents.contains(Constants.QUESTION_INTENT)) {
			return Constants.QUESTION_INTENT;
		}
		
		if(intents.contains(Constants.TBS_INTENT)) {
			return Constants.TBS_INTENT;
		}
		
		return Constants.FORMULA_INTENT;
	}
	
}

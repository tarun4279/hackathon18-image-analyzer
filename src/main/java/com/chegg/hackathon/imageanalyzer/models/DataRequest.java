package com.chegg.hackathon.imageanalyzer.models;

import java.util.List;

public class DataRequest {

	private String intent;
	
	private List<String> queries;

	public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}

	public List<String> getQueries() {
		return queries;
	}

	public void setQueries(List<String> queries) {
		this.queries = queries;
	}
	
	
	
}

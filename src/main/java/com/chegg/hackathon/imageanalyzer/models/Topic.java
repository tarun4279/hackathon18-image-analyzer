package com.chegg.hackathon.imageanalyzer.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Topic {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("organicUrl")
	private String organicUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrganicUrl() {
		return organicUrl;
	}
	public void setOrganicUrl(String organicUrl) {
		this.organicUrl = organicUrl;
	}
	
	
	
}

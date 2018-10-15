package com.chegg.hackathon.imageanalyzer.models;

import java.util.List;

public class LambdaEventEntity {

	
	private String imageUrl;
	private List<String> tags;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
}

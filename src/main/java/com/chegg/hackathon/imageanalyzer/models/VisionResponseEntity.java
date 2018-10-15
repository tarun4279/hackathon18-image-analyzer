package com.chegg.hackathon.imageanalyzer.models;

public class VisionResponseEntity {

	private final String transcribedData;
	private final Float score;
	
	public VisionResponseEntity(String data, float score) {
		this.transcribedData = data;
		this.score = score;
	}

	public String getTranscribedData() {
		return transcribedData;
	}

	public Float getScore() {
		return score;
	}
	
}

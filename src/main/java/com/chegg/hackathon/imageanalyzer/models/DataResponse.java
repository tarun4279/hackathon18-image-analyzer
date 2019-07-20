package com.chegg.hackathon.imageanalyzer.models;

public class DataResponse {

	/**
	 * Image Details
	 */
	private String imageUrl;
	private String transcribedData;
	
	
	/**
	 * Text Details
	 */
	private String text;
	
	/**
	 * Data in Url format
	 */
	private String dataUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTranscribedData() {
		return transcribedData;
	}

	public void setTranscribedData(String transcribedData) {
		this.transcribedData = transcribedData;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	
}

package com.chegg.hackathon.imageanalyzer.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import io.searchbox.annotations.JestId;

public class ImageEntity extends Entity{

	
	@JestId
	private String imageId;

	
	/**
	 * Image Info
	 */
	private String imageUrl;
	private MultipartFile imageData;
	
	
	/**
	 * Image Transcription Fields
	 */
	private String imageTranscriptionData;
	private Float imageTranscriptionScore;
	private List<String> transcriptionTags;
	
	/**
	 * Profanity Data
	 */
	private boolean isProfanity;
	private List<String> profanityTags;
	
	/**
	 * Image Category Data - Chegg Related
	 */
	private Integer cheggSubjectId;
	private String cheggSource;
	private List<String> cheggTags;
	
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public MultipartFile getImageData() {
		return imageData;
	}

	public void setImageData(MultipartFile imageData) {
		this.imageData = imageData;
	}

	public String getImageTranscriptionData() {
		return imageTranscriptionData;
	}

	public void setImageTranscriptionData(String imageTranscriptionData) {
		this.imageTranscriptionData = imageTranscriptionData;
	}

	public List<String> getTranscriptionTags() {
		return transcriptionTags;
	}

	public void setTranscriptionTags(List<String> transcriptionTags) {
		this.transcriptionTags = transcriptionTags;
	}

	public boolean isProfanity() {
		return isProfanity;
	}

	public void setProfanity(boolean isProfanity) {
		this.isProfanity = isProfanity;
	}

	public List<String> getProfanityTags() {
		return profanityTags;
	}

	public void setProfanityTags(List<String> profanityTags) {
		this.profanityTags = profanityTags;
	}

	public Integer getCheggSubjectId() {
		return cheggSubjectId;
	}

	public void setCheggSubjectId(Integer cheggSubjectId) {
		this.cheggSubjectId = cheggSubjectId;
	}

	public String getCheggSource() {
		return cheggSource;
	}

	public void setCheggSource(String cheggSource) {
		this.cheggSource = cheggSource;
	}

	public List<String> getCheggTags() {
		return cheggTags;
	}

	public void setCheggTags(List<String> cheggTags) {
		this.cheggTags = cheggTags;
	}

	public Float getImageTranscriptionScore() {
		return imageTranscriptionScore;
	}

	public void setImageTranscriptionScore(Float imageTranscriptionScore) {
		this.imageTranscriptionScore = imageTranscriptionScore;
	}

	@Override
	public String toString() {
		return new StringBuilder(String.valueOf(imageUrl))
				.append(cheggSubjectId)
				.append(cheggSource)
				.append(cheggTags)
				.toString();
	}


}

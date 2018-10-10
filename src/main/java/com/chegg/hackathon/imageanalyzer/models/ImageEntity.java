package com.chegg.hackathon.imageanalyzer.models;

import java.util.List;

import io.searchbox.annotations.JestId;

public class ImageEntity extends Entity{

	
	@JestId
	private String ImageId;

	
	/**
	 * Image Info
	 */
	private String imageUrl;
	private String imageData;
	
	
	/**
	 * Image Transcription Fields
	 */
	private String imageTranscriptionData;
	private List<String> transcriptionTags;
	
	/**
	 * Profanity Data
	 */
	private boolean isProfanity;
	private List<String> profanityTags;
	
	/**
	 * Image Category Data - Chegg Related
	 */
	private int cheggSubjectId;
	private String cheggSource;
	private List<String> cheggTags;
	
	@Override
	public String toString() {
		return imageUrl;
	}

	@Override
	public String getIndexName() {
		return "images";
	}

}

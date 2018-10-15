package com.chegg.hackathon.imageanalyzer.models;

public  class GenericResponse<T> {

	
	private T response;
	private String errorMessage;
	private Integer errorCode;
	
	public final GenericResponse<T> setResponse(T response) {
		this.response = response;
		return this;
	}
	
	public final GenericResponse<T> setErrorDetails(String message, int code) {
		this.errorMessage = message;
		this.errorCode = code;
		return this;
	}

	public final T getResponse() {
		return response;
	}

	public final String getErrorMessage() {
		return errorMessage;
	}

	public final Integer getErrorCode() {
		return errorCode;
	}
	
}

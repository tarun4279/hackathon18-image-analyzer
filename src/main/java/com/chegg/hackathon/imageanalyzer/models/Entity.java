package com.chegg.hackathon.imageanalyzer.models;


/**
 * A class to represent an Elastic search db entity
 * extended by other custom entities
 * @author tarun.kundhiya
 *
 */
public abstract class Entity {
	
	private String indexName;
	
	public String getIndexName() {
		return this.indexName;
	}
	
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
		
}

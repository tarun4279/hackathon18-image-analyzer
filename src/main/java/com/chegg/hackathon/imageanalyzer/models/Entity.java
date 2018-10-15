package com.chegg.hackathon.imageanalyzer.models;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class to represent an Elastic search db entity
 * extended by other custom entities
 * @author tarun.kundhiya
 *
 */
public abstract class Entity {
	
	public abstract String getIndexName();
		
	@SuppressWarnings("unchecked")
	public Map<String,String> getEntityData() {
		
		ObjectMapper m = new ObjectMapper();
		
		Map<String,String> data = m.convertValue(this, Map.class);
		data.entrySet().removeIf(e -> StringUtils.isEmpty(e.getValue()));
		
		return data;
	}
}

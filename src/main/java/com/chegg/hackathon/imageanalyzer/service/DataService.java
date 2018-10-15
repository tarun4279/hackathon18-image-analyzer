package com.chegg.hackathon.imageanalyzer.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.chegg.hackathon.imageanalyzer.models.Entity;
import com.chegg.hackathon.imageanalyzer.models.GenericResponse;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

@Service
public class DataService {

	
	private static final int JEST_CLIENT_READ_TIMEOUT = 10000;
	private static final int JEST_CLIENT_CONNECTION_TIMEOUT = 10000;
	
	private JestClient jestClient;
	private String baseUrl;
	
	final static Logger logger = LoggerFactory.getLogger(DataService.class);
	
	
	public DataService(String baseUrl) {
	
		 logger.info("Creating a Elastic search Data Service client for " + baseUrl);
		
		 JestClientFactory jestClientFactory = new JestClientFactory();
		    jestClientFactory.setHttpClientConfig(new HttpClientConfig.Builder(baseUrl)
		        .multiThreaded(true).readTimeout(JEST_CLIENT_READ_TIMEOUT)
		        .connTimeout(JEST_CLIENT_CONNECTION_TIMEOUT).build());
		 
		 this.jestClient = jestClientFactory.getObject();
		 this.baseUrl = baseUrl;
		
	}
	
	public GenericResponse<DocumentResult> storeData(Object object, String indexName, String dataType) {
		
		Index index = new Index.Builder(object).index(indexName).type(dataType).build();
		
		try {
			return new GenericResponse<DocumentResult>().setResponse(jestClient.execute(index));
		} catch (IOException e) {
			logger.error("Save failed due to for {} due to {}" , object, e.getMessage());
			return new GenericResponse<DocumentResult>().setErrorDetails("Save failed due to " + e.getMessage(), -1);
		}
		
	}
	
	
	public <T> List<T> search (Entity entity, String dataType) throws IOException {
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

		Map<String,String> queryData = entity.getEntityData();
		
		String[] fieldNames = (String[]) Arrays.copyOf(queryData.keySet().toArray(), queryData.keySet().size());
		String[] values = (String[]) Arrays.copyOf(queryData.values().toArray(), queryData.values().size());
		
		
		searchSourceBuilder
        .query(QueryBuilders.multiMatchQuery(values, fieldNames));
        
		Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(entity.getIndexName())
                .addType(dataType)
                .build();
 
		return (List<T>) this.jestClient.execute(search).getHits(entity.getClass()); 
	}
	
	public String getName() {
		return this.baseUrl;
	}
	
	
}

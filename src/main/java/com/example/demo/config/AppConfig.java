package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class AppConfig {
	
	@Value("${product.mongo.hostname}")
	public String hostName;
	
	@Value("${product.mongo.portnumber}")
	public int port;
	
	@Bean
	public MongoClient returnMongoClient() {
		return new MongoClient(hostName, port);
	}

}

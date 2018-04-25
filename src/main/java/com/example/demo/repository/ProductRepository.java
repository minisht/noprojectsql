package com.example.demo.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

@Component
public class ProductRepository {
	
	@Autowired
	public MongoClient mongoClient;
	
	@Value("${product.mongo.database}")
	public String databaseName;
	
	@Value("${product.mongo.collection}")
	public String collectionName;
	
	
	public String getProductPriceByID(String productID) {
		DBCollection table = mongoClient.getDB(databaseName).getCollection(collectionName);
		BasicDBObject obj = new BasicDBObject();
		
		obj.put("productID", productID);
		DBCursor cursor = table.find(obj);
		
		while(cursor.hasNext()) {
			return cursor.next().toString();
		}
		
		return null;
	}
	
	public String updateProductByID(String id, String currentPrice, String currencyCode) {
		DBCollection table = mongoClient.getDB(databaseName).getCollection(collectionName);
		BasicDBObject objOld = new BasicDBObject();
		BasicDBObject objNew = new BasicDBObject();
		
		objOld.put("productID", id);
		objNew.put("productID", id);
		objNew.put("current_price", currentPrice);
		objNew.put("currency_code", currencyCode);
		
		table.update(objOld, objNew);
		
		return currencyCode + currentPrice;
	}

}

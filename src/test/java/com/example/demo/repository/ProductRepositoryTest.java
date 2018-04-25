package com.example.demo.repository;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ProductRepositoryTest {
	
	@Mock
	MongoClient mongoClient;
	
	@Mock
	DB db;
	
	@Mock
	DBCollection table;
	
	@Mock
	DBCursor cursor;
	
	String databaseName = "test";
	
	String collectionName = "productprice";
	
	@InjectMocks
	ProductRepository productRepository;
	
	@Before
	public void inject() {
		MockitoAnnotations.initMocks(this); //This is to create mocked classes for @mock
		productRepository = new ProductRepository();
		productRepository.mongoClient = mongoClient;
		productRepository.collectionName=collectionName;
		productRepository.databaseName=databaseName;
	}

	@Test
	public void testGetProdById() {	
		BasicDBObject returnObj = new BasicDBObject();
		returnObj.put("price", 123);
		
		Mockito.when(mongoClient.getDB(databaseName)).thenReturn(db);
		Mockito.when(db.getCollection(collectionName)).thenReturn(table);
		Mockito.when(table.find(Mockito.any(DBObject.class))).thenReturn(cursor);
		Mockito.when(cursor.hasNext()).thenReturn(true);
		Mockito.when(cursor.next()).thenReturn(returnObj);
		
       String output = productRepository.getProductPriceByID("someid");
       Assert.assertTrue(output.contains("price"));
	}
	
	@Test
	public void testGetProdByIdNull() {	
		BasicDBObject returnObj = new BasicDBObject();
		returnObj.put("price", 123);
		
		Mockito.when(mongoClient.getDB(databaseName)).thenReturn(db);
		Mockito.when(db.getCollection(collectionName)).thenReturn(table);
		Mockito.when(table.find(Mockito.any(DBObject.class))).thenReturn(cursor);
		Mockito.when(cursor.hasNext()).thenReturn(false);
		Mockito.when(cursor.next()).thenReturn(returnObj);
		
       String output = productRepository.getProductPriceByID("someid");
       Assert.assertNull(output);
	}

	
	@Test
	public void testUpdateProductById() {	
		
		Mockito.when(mongoClient.getDB(databaseName)).thenReturn(db);
		Mockito.when(db.getCollection(collectionName)).thenReturn(table);
		Mockito.when(table.update(Mockito.any(DBObject.class), Mockito.any(DBObject.class))).thenReturn(Mockito.any());
		
       String output = productRepository.updateProductByID("someid","currentPrice","currencyCode");
       Assert.assertTrue(output.contains("currentPrice"));
	}
}

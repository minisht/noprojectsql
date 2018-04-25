package com.example.demo.inttests;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.demo.controller.ProductController;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import junit.framework.Assert;

public class ProductAPIIntegrationTests {

	static MongodExecutable mongodExecutable = null;

	@BeforeClass
	public static void testStandard() throws IOException {
		// ->
		MongodStarter starter = MongodStarter.getDefaultInstance();

		int port = 27018;
		IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
				.net(new Net(port, Network.localhostIsIPv6())).build();

		try {
			mongodExecutable = starter.prepare(mongodConfig);
			MongodProcess mongod = mongodExecutable.start();

			MongoClient mongo = new MongoClient("localhost", port);
			DB db = mongo.getDB("test");
			BasicDBObject obj = new BasicDBObject();
			obj.put("productID", "13860428");
			obj.put("name", "somename");
			obj.put("current_price", 123);
			obj.put("currency_code", "USD");

			DBCollection col = db.createCollection("product", new BasicDBObject());
			col.save(obj);

		} finally {

		}
	}


	MongoClient mongoClient = null;

	ProductRepository productRepository = null;

	ProductServiceImpl productServiceImpl = null;

	ProductController productController = null;

	@Test
	public void test() throws Exception {
		mongoClient = new MongoClient("localhost", 27018);
		productRepository = new ProductRepository();
		productRepository.collectionName = "product";
		productRepository.databaseName = "test";
		productRepository.mongoClient = mongoClient;

		productServiceImpl = new ProductServiceImpl();
		productServiceImpl.productRepository = productRepository;
		productServiceImpl.productID = "$['product']['available_to_promise_network']['product_id']";
		productServiceImpl.currency = "$['currency_code']";
		productServiceImpl.currentPrice = "$['current_price']";
		productServiceImpl.productTitle = "$['product']['item']['product_description']['title']";
		productServiceImpl.updateCurrency = "$['current_price']['currency']";
		productServiceImpl.updateCurrentPrice = "$['current_price']['currentPrice']";

		productController = new ProductController();
		productController.productService = productServiceImpl;
		String output = productController.getProductDetails("13860428");
		// System.out.println(output);
		Assert.assertTrue(output.contains("Lebowski"));
		String updateProductDetails = productController.updateProductDetails("13860428",
				"{\n" + "  \"id\": \"13860428\",\n" + "  \"name\": \"The Big Lebowski (Blu-ray)\",\n"
						+ "  \"current_price\": {\n" + "    \"currentPrice\": \"$14.50\",\n"
						+ "    \"currency\": \"USD\"\n" + "  }\n" + "}");
		Assert.assertTrue(updateProductDetails.contains("14.50"));

	}
	

	@AfterClass
	public static void stopMongo() {
		if (mongodExecutable != null)
			mongodExecutable.stop();
	}

}

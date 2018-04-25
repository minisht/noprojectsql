package com.example.demo.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

import junit.framework.Assert;

public class ProductServiceImplTest {

	@Mock
	ProductRepository productRepository;
	
	String productID = "$['product']['available_to_promise_network']['product_id']";

	String productTitle = "$['product']['item']['product_description']['title']";

	String currentPrice = "$['current_price']['currentPrice']";

	String currency = "$['current_price']['currency']";

	String updateCurrentPrice = "$['current_price']['currentPrice']";

	String updateCurrency = "$['current_price']['currency']";

	@InjectMocks
	ProductServiceImpl productServiceImpl;

	@Before
	public void inject() {
		MockitoAnnotations.initMocks(this);
		productServiceImpl = new ProductServiceImpl();
		productServiceImpl.productRepository = productRepository;
		productServiceImpl.productID = productID;
		productServiceImpl.productTitle = productTitle;
		productServiceImpl.currentPrice = currentPrice;
		productServiceImpl.currency = currency;
		productServiceImpl.updateCurrentPrice = updateCurrentPrice;
		productServiceImpl.updateCurrency = updateCurrency;
	}

	@Test
	public void testGetProductsById() throws Exception {
		Mockito.when(productRepository.getProductPriceByID("13860428"))
				.thenReturn("{\n" + "  \"id\": \"13860428\",\n" + "  \"name\": \"The Big Lebowski (Blu-ray)\",\n"
						+ "  \"current_price\": {\n" + "    \"currentPrice\": \"$14.10\",\n"
						+ "    \"currency\": \"USD\"\n" + "  }\n" + "}");
		Product output = productServiceImpl.getProductsByID("13860428");
		Assert.assertEquals("The Big Lebowski (Blu-ray)", output.getName());

	}
	
	@Test(expected = Exception.class)
	public void testGetProductsByIdNotFound() throws Exception {
		Mockito.when(productRepository.getProductPriceByID("13860428"))
				.thenReturn("{\n" + "  \"id\": \"13860428\",\n" + "  \"name\": \"The Big Lebowski (Blu-ray)\",\n"
						+ "  \"current_price\": {\n" + "    \"currentPrice\": \"$14.10\",\n"
						+ "    \"currency\": \"USD\"\n" + "  }\n" + "}");
		Product output = productServiceImpl.getProductsByID("13860450");
	}

	@Test(expected = Exception.class)
	public void testGetProductsException() throws Exception {
		Mockito.when(productRepository.getProductPriceByID("13860428")).thenThrow(new RuntimeException());
		productServiceImpl.getProductsByID("13860428");
	}

	@Test
	public void testUpdateProductDetails() {
		Mockito.when(productRepository.updateProductByID("someID", "$14.10", "USD")).thenReturn("someValue");
		String output = productServiceImpl.updateProductDetails("someID",
				"{\n" + "  \"id\": \"13860428\",\n" + "  \"name\": \"The Big Lebowski (Blu-ray)\",\n"
						+ "  \"current_price\": {\n" + "    \"currentPrice\": \"$14.10\",\n"
						+ "    \"currency\": \"USD\"\n" + "  }\n" + "}");
		Assert.assertEquals("someValue", output);
	}

}

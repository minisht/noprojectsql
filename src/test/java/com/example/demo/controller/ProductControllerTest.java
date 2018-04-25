package com.example.demo.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.google.gson.Gson;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ProductControllerTest {
	
	@Mock
	ProductService productService;
	
	MockMvc mockMVC;
	
	@InjectMocks
	ProductController productController;
	
	@Before
	public void inject() {
		MockitoAnnotations.initMocks(this);
		productController = new ProductController();
		productController.productService = productService;
		mockMVC = MockMvcBuilders.standaloneSetup(productController).build();
	}
	
	@Test
	public void testGetProductDetails() throws Exception {
		Product p = new Product();
		Mockito.when(productService.getProductsByID("someID")).thenReturn(p);
		
		mockMVC.perform(get("/product/{id}", "someID")).andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateProductDetails() throws Exception{
		Mockito.when(productService.updateProductDetails("someID", "body")).thenReturn("$123");
		mockMVC.perform(put("/product/{id}", "someID").content("body")).andExpect(status().isOk())
		.andExpect(content().bytes("body".getBytes()));
	}


}

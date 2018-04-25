package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import com.google.gson.Gson;

@RestController
public class ProductController {
	
	@Autowired
	public ProductService productService;
	
	@GetMapping(path = "/product/{id}")
	public String getProductDetails(@PathVariable(name="id") String id ) throws Exception {
		String output = new Gson().toJson(productService.getProductsByID(id));
		return output;
	}
	
	@PutMapping(path = "/product/{id}")
	public String updateProductDetails(@PathVariable(name="id") String id, @RequestBody String body) throws Exception{
		String output = new Gson().toJson(productService.updateProductDetails(id, body));
		return body;
	}

}

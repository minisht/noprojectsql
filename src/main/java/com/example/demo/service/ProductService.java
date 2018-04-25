package com.example.demo.service;

import com.example.demo.entity.Product;

public interface ProductService {
	
	Product getProductsByID(String id) throws Exception;
	String updateProductDetails(String id, String body);

}



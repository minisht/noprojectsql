package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.DataStoreUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	public ProductRepository productRepository;
	
	@Value("${datastore.prod.json.path}")
	public String productTitle;
	
	@Value("${datastore.prod.json.path.productid}")
	public String productID;
	
	@Value("${datastore.prod.json.path.current_price}")
	public String currentPrice;
	
	@Value("${datastore.prod.json.path.currency}")
	public String currency;
	
	@Value("${datastore.prod.json.path.updateprice}")
	public String updateCurrentPrice;
	
	@Value("${datastore.prod.json.path.updatecurrency}")
	public String updateCurrency;
	
	
	@Override
	public Product getProductsByID(String id) throws Exception {
		
		String dataJSON = FileUtils.readFileToString(new ClassPathResource("data.json").getFile());
		
		String productID = DataStoreUtil.executeJsonPath(dataJSON, this.productID);
		if(!productID.equals(id)) {
			throw new RuntimeException("ProductID mismatch");
		}
		
		try {	
		String productName = DataStoreUtil.executeJsonPath(dataJSON, productTitle);
		String docFromDB = productRepository.getProductPriceByID(id);
		String price = DataStoreUtil.executeJsonPath(docFromDB, currentPrice);
		String currency = DataStoreUtil.executeJsonPath(docFromDB, this.currency);
		Product p = new Product();
		p.setId(id);
		p.setName(productName);
		Map<String, String> m = new HashMap<String, String>();
		m.put("currentPrice", price);
		m.put("currency", currency);
		p.setCurrent_price(m);
		return p;
		} catch (Exception e){
			throw new RuntimeException("Exception occurred in getting the product details " + id);
		}
	}

	@Override
	public String updateProductDetails(String id, String body) {
		String currentPrice = DataStoreUtil.executeJsonPath(body, updateCurrentPrice);
		String currency = DataStoreUtil.executeJsonPath(body, updateCurrency);
		return productRepository.updateProductByID(id, currentPrice, currency);
		
	}

}

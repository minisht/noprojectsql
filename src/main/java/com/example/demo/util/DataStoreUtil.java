package com.example.demo.util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class DataStoreUtil {
	
	public static String executeJsonPath(String json, String jsonPath) {
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		return JsonPath.read(document, jsonPath).toString();
		
	}

}

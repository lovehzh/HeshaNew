package com.hesha.utils;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	public static String genJson(Object object) {
		String json = "";
		ObjectMapper mapper =  new ObjectMapper();
		StringWriter writer = new StringWriter();  
		JsonGenerator gen = null;
		try {
			gen = new JsonFactory().createJsonGenerator(writer);
			mapper.writeValue(gen, object);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				gen.close();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		json = writer.toString();
		return json;
	}
}

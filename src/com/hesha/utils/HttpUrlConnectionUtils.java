package com.hesha.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;


public class HttpUrlConnectionUtils {
	private static final String TAG = "HttpUrlConnectionUtils";
	public static String  post(String endpoint, String parameters, String contentType) throws IOException{
		String result = "";
		URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
       
        byte[] bytes = parameters.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(1800000);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
//                    "application/x-www-form-urlencoded;charset=UTF-8");
            		contentType);
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status > 199 && status < 300) {
            	InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            	BufferedReader reader = new BufferedReader(isr);
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            result += line;
    	        }
    	        isr.close();
    	        reader.close();
              
            }else {
            	result = "Server error code :" + status;
             }
        } catch (SocketTimeoutException e) {
        	e.printStackTrace();
        	return Constants.CONNECTION_TIMED_OUT;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        
        return result;
      }
	
	public static String  get(String endpoint, String parameters) throws IOException{
		String result = "";
		URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
       
        byte[] bytes = parameters.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(1800000);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            		
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status > 199 && status < 300) {
            	InputStreamReader isr = new InputStreamReader(conn.getInputStream());
            	BufferedReader reader = new BufferedReader(isr);
    	        String line;
    	        while ((line = reader.readLine()) != null) {
    	            result += line;
    	        }
    	        isr.close();
    	        reader.close();
              
            }else {
            	result = "Server error code :" + status;
             }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        
        return result;
      }
}

package com.hesha.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Exception中，先输出日志，再assertEquals(true, false);
 * */
public class DtoUtils {
	static private byte[] key = "g1i2g3a4b5u6d".getBytes();
	
//	/**
//	 * string 变为DTO
//	 * */
//	static public <T> T fromJson(final String jsonString, final Class<T> cls) {
//		return Json.fromJson(cls, jsonString);
//	}

	static public String getCurDateYYYYMMDDHHMMSS() {

		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	}

	/***
	 * 将encode string变为decode string
	 * */
	static public String toDecode(final String encodeParam) {
		try {
			return new String(XXTEA.decrypt(Base64.decode(encodeParam), key));
		} catch (Exception e) {
            return null;
		}
	}

//	/***
//	 * 将encode string变为DTO
//	 * */
//	static public String toDecode(final String encodeParam, final Class<T> cls)
//			 {
//		Log.i("DtoUtils.toDecode", "encodeParam = " + encodeParam);
//		try {
//			String jsonString = new String(XXTEA.decrypt(Base64
//					.decode(encodeParam), key));
//			return jsonString;
//		} catch (Exception e) {
//			Log.e("DtoUtils.toDecode", e.getMessage());
//            return null;
//		}
//	}

	/***
	 * 将DTO变为encode string
	 * */
	static public String toEncode(final String obj) {
		if (obj instanceof String) {
			return Base64.encode(XXTEA.encrypt(String.valueOf(obj).getBytes(),
					key));
		}
		return Base64.encode(XXTEA.encrypt(obj.getBytes(), key));
	}

//	/***
//	 * 将DTO变为json string
//	 * */
//	static public String toJson(final Object obj) {
//		return Json.toJson(obj, JsonFormat.compact());
//
//	}
}

package com.hesha.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hesha.bean.BaseItem;
import com.hesha.bean.PhotoItem;
import com.hesha.bean.SubjectItem;
import com.hesha.bean.errorcode.ErrorCode;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


public class Utils {
	
	public static String inputStream2String (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
	
	public static String parseCode(Context context, String code) {
		ArrayList<ErrorCode> codes = getErrorCodes(context);
		for(int i=0; i<codes.size(); i++) {
			ErrorCode errorCode = codes.get(i);
			if(errorCode.getKey().equals(code)) {
				return  errorCode.getValue();
			}
		}
		return code;
	}
	
	public static ArrayList<ErrorCode> getErrorCodes(Context context) {
		ArrayList<ErrorCode> codes = new ArrayList<ErrorCode>();
		AssetManager manager = context.getAssets();
		InputStream is = null;
	
		ObjectMapper mapper = new ObjectMapper();
		try {
			is = manager.open("error_code.txt");
			
			String reuslt = inputStream2String(is);
			Log.i("xx", reuslt);
			
			codes = mapper.readValue(reuslt, new TypeReference<ArrayList<ErrorCode>>() {
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i("xx", "size: " + codes.size());
		return codes;
	}
	
	public static String decodeSpecial(String str){
    	String result = str
    	.replaceAll("&amp;", "&")
    	.replaceAll("&lt;", "<")
		.replaceAll("&gt;", ">")
		.replaceAll("&apos;", "'")
		.replaceAll("&#39;", "'")
		.replaceAll("&#039;", "'")
		.replaceAll("&quot;", "\"")
		.replaceAll("&#13;&#10;", "\n")
		;
    	return result;
    }
	
	
	public static int getMaxIndexForPageSize(int pagesize) {
		int result = 0;
		if(pagesize == Constants.PAGE_SIZE + 1) {
			result = pagesize - 2;
		}else {
			result =  pagesize;
		}
		return result;
	}
	
	
	
//	public static int getResId(int value) {
//		
//		switch (value) {
//		case 1:
//			return R.drawable.grade_10;
//		case 2:
//			return R.drawable.grade_20;
//		case 3:
//			return R.drawable.grade_30;
//		case 4:
//			return R.drawable.grade_40;
//		case 5:
//			return R.drawable.grade_50;
//		case 6:
//			return R.drawable.grade_60;
//		case 7:
//			return R.drawable.grade_70;
//		case 8:
//			return R.drawable.grade_80;
//		case 9:
//			return R.drawable.grade_90;
//		case 10:
//			return R.drawable.grade_100;	
//
//		default:
//			return R.drawable.grade_off2;
//		}
//	}
//	
//	public static int getStringValue(String str) {
//		int result = 0;
//		double d = 0.0;
//		try {
//			d = Double.valueOf(str);
//		} catch(NumberFormatException e) {
//			d = 60.0;
//		}
//		result = (int)d / 10;
//		return result;
//	}
	
	
	public static int getStringValue2(String str) {
		int result = 0;
		double d = 0.0;
		try {
			d = Double.valueOf(str);
		} catch(NumberFormatException e) {
			d = 60.0;
		}
		result = (int)d;
		return result;
	}
	
	public static BaseItem getRealBaseItem(BaseItem baseItem) {
		if(baseItem instanceof PhotoItem) {//bug ?
			baseItem.setItem_type(1);
		}else if(baseItem instanceof SubjectItem) {
			baseItem.setItem_type(2);
		}else {
			baseItem.setItem_type(3);
		}
		
		return baseItem;
	}
	
}

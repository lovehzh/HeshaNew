package com.hesha.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceInfo {
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	String deviceId = tm.getDeviceId();
    	if(null != deviceId) {
    		return deviceId;
    	}
        return "unknown_device";
	}
}

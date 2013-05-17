/*
 * Class: NetState
 * --------------------------------------------------
 * Version Information:
 * $Id$
 * $HeadURL$
 * $Revision$
 * $Author$
 * $Date$
 * --------------------------------------------------
 *
 * Copyright 2008-2011, Gigabud Limited
 *
 * Licensed under the Gigabud License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gigabud.com/licenses/LICENSE-1.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * {PLEASE REPLACE ACTUAL LICENSE BLOCK HERE}
 * 
 */
package com.hesha.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <p>
 * A description to be placed here....
 *
 * @author hzh
 * @since 2011-3-25
 * @version $Revision$
 */
public class NetState {
	/**
	 * 检测网络是否连接（注：需要在配置文件即AndroidManifest.xml加入权限）
	 * 
	 * @param context
	 * @return true : 网络连接成功
	 * @return false : 网络连接失败
	 * */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
	
	/*  
	*@return boolean return true if the application can access the internet  
	*/  
//	private boolean haveInternet(){   
//	    NetworkInfo info=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE).getActiveNetworkInfo();   
//	    if(info==null || !info.isConnected()){   
//	        return false;   
//	    }   
//	    if(info.isRoaming()){   
//	        //here is the roaming option you can change it if you want to disable internet while roaming, just return false   
//	        return true;   
//	    }   
//	    return true;   
//	}  
	
	/**
	 * 
	 * @param activity
	 * @return boolean return true if the application can access the internet
	 */
	public static boolean hasInternet(Activity activity) {
		ConnectivityManager manager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return true;
		}
		return true;
 
	}

}

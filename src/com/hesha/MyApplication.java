package com.hesha;

import java.util.ArrayList;

import com.hesha.bean.Collection;

import android.app.Application;

public class MyApplication extends Application{
	private ArrayList<Collection> myCollections;

	public ArrayList<Collection> getMyCollections() {
		return myCollections;
	}

	public void setMyCollections(ArrayList<Collection> myCollections) {
		this.myCollections = myCollections;
	}
	
}

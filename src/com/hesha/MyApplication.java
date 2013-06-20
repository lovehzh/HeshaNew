package com.hesha;

import java.util.ArrayList;

import com.hesha.bean.Collection;
import com.hesha.bean.User;

import android.app.Application;

public class MyApplication extends Application{
	private User user;
	private ArrayList<Collection> myCollections;

	public ArrayList<Collection> getMyCollections() {
		return myCollections;
	}

	public void setMyCollections(ArrayList<Collection> myCollections) {
		this.myCollections = myCollections;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}

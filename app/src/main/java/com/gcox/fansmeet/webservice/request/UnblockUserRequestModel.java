package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnblockUserRequestModel {

	@SerializedName("UserName")
	@Expose
	private String mUserName;

	@SerializedName("userId")
	@Expose
	private int mUnBlockUserId;

	public void setUserName(String userName){
		this.mUserName = userName;
	}

	public String getUserName(){
		return mUserName;
	}

	public void setUnBlockUserId(int unBlockUserId){
		this.mUnBlockUserId = unBlockUserId;
	}

	public int getUnBlockUserId(){
		return mUnBlockUserId;
	}
}
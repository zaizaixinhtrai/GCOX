package com.gcox.fansmeet.features.blocked_screen;

import com.gcox.fansmeet.core.adapter.DisplayableItem;
import com.google.gson.annotations.SerializedName;

public class BlockedUserModel implements DisplayableItem {

	@SerializedName("UserName")
	private String userName;

	@SerializedName("UserImage")
	private String userImage;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("DisplayName")
	private String displayName;

	@SerializedName("WebProfileUrl")
	private String webProfileUrl;

	@SerializedName("Gender")
	private String gender;

	@SerializedName("IsFollow")
	private boolean isFollow;


	@SerializedName("PhoneNumber")
	public String phoneNumber="";

	@SerializedName("NormalizedPhone")
	public String normalizedPhone="";

	private boolean isBlocked = true;//default value is blocked

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserImage(String userImage){
		this.userImage = userImage;
	}

	public String getUserImage(){
		return userImage;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName(){
		return displayName;
	}

	public void setWebProfileUrl(String webProfileUrl){
		this.webProfileUrl = webProfileUrl;
	}

	public String getWebProfileUrl(){
		return webProfileUrl;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setIsFollow(boolean isFollow){
		this.isFollow = isFollow;
	}

	public boolean getIsFollow(){
		return isFollow;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean blocked) {
		isBlocked = blocked;
	}

	@Override
	public String toString() {
		return "BlockedUserModel{" +
				"userName='" + userName + '\'' +
				", userImage='" + userImage + '\'' +
				", userId='" + userId + '\'' +
				", displayName='" + displayName + '\'' +
				", webProfileUrl='" + webProfileUrl + '\'' +
				", gender='" + gender + '\'' +
				", isFollow=" + isFollow +
				", phoneNumber='" + phoneNumber + '\'' +
				", normalizedPhone='" + normalizedPhone + '\'' +
				", isBlocked=" + isBlocked +
				'}';
	}
}
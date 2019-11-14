package com.gcox.fansmeet.util.instagram;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstagramModel {

	@SerializedName("website")
	@Expose
	private String website;

	@SerializedName("full_name")
	@Expose
	private String fullName;

	@SerializedName("bio")
	@Expose
	private String bio;

	@SerializedName("profile_picture")
	@Expose
	private String profilePicture;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("username")
	@Expose
	private String username;

	public void setWebsite(String website){
		this.website = website;
	}

	public String getWebsite(){
		return website;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return bio;
	}

	public void setProfilePicture(String profilePicture){
		this.profilePicture = profilePicture;
	}

	public String getProfilePicture(){
		return profilePicture;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

}
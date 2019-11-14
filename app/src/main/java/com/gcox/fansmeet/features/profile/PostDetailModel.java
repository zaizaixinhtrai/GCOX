package com.gcox.fansmeet.features.profile;

import com.gcox.fansmeet.models.ItemClassComments;

import java.util.ArrayList;

/**
 * Created by User on 11/11/2015.
 */
public class PostDetailModel {

    public String getId() {
        return PostId;
    }

    public void setId(String id) {
        this.PostId = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getUser_id() {
        return UserId;
    }

    public void setUser_id(String user_id) {
        this.UserId = user_id;
    }

    public String getUsername() {
        return UserName;
    }

    public void setUsername(String username) {
        this.UserName = username;
    }

    public String getDisplay_name() {
        return DisplayName;
    }

    public void setDisplay_name(String display_name) {
        this.DisplayName = display_name;
    }

    public int getLike_count() {
        return LikeCount;
    }

    public void setLike_count(int like_count) {
        this.LikeCount = like_count;
    }

    public int getIs_like() {
        return IsLike;
    }

    public void setIs_like(int is_like) {
        this.IsLike = is_like;
    }

    public int getIs_report() {
        return IsReport;
    }

    public void setIs_report(int is_report) {
        this.IsReport = is_report;
    }

    public int getIs_follow() {
        return IsFollow;
    }

    public void setIs_follow(int is_follow) {
        this.IsFollow = is_follow;
    }

    public String getUser_image() {
        return UserImage;
    }

    public void setUser_image(String user_image) {
        this.UserImage = user_image;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public int getMedia_type() {
        return MediaType;
    }

    public void setMedia_type(int media_type) {
        this.MediaType = media_type;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongtitude() {
        return Longitude;
    }

    public void setLongtitude(String longtitude) {
        this.Longitude = longtitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getMedia_image() {
        return MediaImage;
    }

    public void setMedia_image(String media_image) {
        this.MediaImage = media_image;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getMedia_video() {
        return MediaVideo;
    }

    public void setMedia_video(String media_video) {
        this.MediaVideo = media_video;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String created) {
        this.Timestamp = created;
    }

    public int getComment_count() {
        return CommentCount;
    }

    public void setComment_count(int comment_count) {
        this.CommentCount = comment_count;
    }

    public ArrayList<ItemClassComments> getComments() {
        return CommentList;
    }

    public void setComments(ArrayList<ItemClassComments> comments) {
        this.CommentList = comments;
    }

    private String PostId;
    private String Title;
    private String UserId;
    private String UserName;
    private String DisplayName;
    private int LikeCount;
    private int IsLike;
    private int IsReport;
    private int IsFollow;
    private String UserImage;
    private String Gender;
    private int MediaType;
    private String Latitude;
    private String Longitude;
    private String Address;
    private String MediaImage;
    private String Message;
    private String MediaVideo;
    private String Timestamp;
    private int CommentCount;
    private ArrayList<ItemClassComments> CommentList;
    private String WebPostUrl;
    private String ThumbnailImage;
    private String Created;
    private long ViewCount;

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }

    public String getThumbnailImage() {
        return ThumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        ThumbnailImage = thumbnailImage;
    }


    public String getWebPostUrl() {
        return WebPostUrl;
    }

    public void setWebPostUrl(String webPostUrl) {
        WebPostUrl = webPostUrl;
    }

    public long getViewCount() {
        return ViewCount;
    }

    public void setViewCount(long viewCount) {
        ViewCount = viewCount;
    }
}

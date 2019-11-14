package com.gcox.fansmeet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 4/25/2016.
 */
public class PostDataModel {
    @SerializedName("Id")
    @Expose
    private String PostId;
    @SerializedName("Title")
    @Expose
    private String Title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("PrizeText")
    @Expose
    private String prizeText;
    @SerializedName("HashTag")
    @Expose
    private String hashTag;
    @SerializedName("PostType")
    @Expose
    private int postType;
    @SerializedName("MediaType")
    @Expose
    private int mediaType;
    private String Address;
    private String MediaImage;

    @SerializedName("Video")
    @Expose
    private String MediaVideo;

    private String WebPostUrl;
    @SerializedName("Image")
    @Expose
    private String mediaImageThumbnail;

    @SerializedName("MaxSubmission")
    @Expose
    public int maxSubmission;
    @SerializedName("StartedAt")
    @Expose
    public String startedAt;
    @SerializedName("EndedAt")
    @Expose
    public String endedAt;

    @SerializedName("Caption")
    @Expose
    public String caption;


    public String getPostId() {
        return PostId;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrizeText() {
        return prizeText;
    }

    public String getHashTag() {
        return hashTag;
    }

    public int getPostType() {
        return postType;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getAddress() {
        return Address;
    }

    public String getMediaImage() {
        return MediaImage;
    }

    public String getMediaVideo() {
        return MediaVideo;
    }

    public String getWebPostUrl() {
        return WebPostUrl;
    }

    public String getMediaImageThumbnail() {
        return mediaImageThumbnail;
    }
}

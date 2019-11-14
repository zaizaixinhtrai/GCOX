package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.util.StringUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by User on 9/18/2015.
 */
public class PostCreatePostRequestModel extends TypeOutputModel {
    @SerializedName("Address")
    @Expose
    private String mAddress;
    @SerializedName("Address")
    @Expose
    private double mLatitude;
    @SerializedName("Latitude")
    @Expose
    private double mLongitude;
    @SerializedName("Longitude")
    @Expose
    private String mTitle;
    @SerializedName("Title")
    @Expose
    private int mMediaType;
    @SerializedName("MediaType")
    @Expose
    private File mImage;
    @SerializedName("Image")
    @Expose
    private File mVideo;
    @SerializedName("Video")
    @Expose
    private String mTaggedUsers;
    private boolean isSubmissionChallenge;
    private String tagUsers;


    public PostCreatePostRequestModel(String address,
                                      double latitude,
                                      double longitude,
                                      String title,
                                      int media_type,
                                      File media_image,
                                      File media_video,
                                      boolean isSubmissionChallenge,
                                      String tagUsers) {
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTitle = title;
        this.mMediaType = media_type;
        this.mImage = media_image;
        this.mVideo = media_video;
        this.isSubmissionChallenge = isSubmissionChallenge;
        this.tagUsers = tagUsers;
        handleAddMultiPar(media_type);
    }

    public PostCreatePostRequestModel(String address,
                                      double latitude,
                                      double longitude,
                                      String title,
                                      int media_type,
                                      File media_image,
                                      boolean isSubmissionChallenge,
                                      String tagUsers) {
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTitle = title;
        this.mMediaType = media_type;
        this.mImage = media_image;
        this.isSubmissionChallenge = isSubmissionChallenge;
        this.tagUsers = tagUsers;
        handleAddMultiPar(media_type);
    }

    public PostCreatePostRequestModel(String address,
                                      double latitude,
                                      double longitude,
                                      String title,
                                      int media_type,
                                      boolean isSubmissionChallenge,
                                      String tagUsers) {
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTitle = title;
        this.mMediaType = media_type;
        this.isSubmissionChallenge = isSubmissionChallenge;
        this.tagUsers = tagUsers;
        handleAddMultiPar(media_type);
    }

    private void handleAddMultiPar(int type) {
        addPartNotEmptyString("Address", getAddress());
        addPartNotEmptyString("Latitude", String.valueOf(getLatitude()));
        addPartNotEmptyString("Longitude", String.valueOf(getLongitude()));
        if (isSubmissionChallenge) {
            addPartNotEmptyString("Caption", getTitle());
        } else {
            addPartNotEmptyString("Description", getTitle());
        }
        addPartNotEmptyString("MediaType", String.valueOf(mMediaType));
        addPartNotEmptyString("PostType", isSubmissionChallenge ? "0" : "1");
        if (!StringUtil.isNullOrEmptyString(tagUsers)) addPartNotEmptyString("TagUsers", tagUsers);
        handleAddPartData(type);
    }

    private void handleAddPartData(int type) {

        if (type == Constants.POST_TYPE_IMAGE || type == Constants.POST_TYPE_CHALLENGE_SELFIE) {

            if (mImage != null) {
//                TypedFile fileUpload = new TypedFile("image/png", getMedia_image());
                mTypeOutput.addFormDataPart("Image", "image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"), getMedia_image()));
            }
        }

        if (type == Constants.POST_TYPE_VIDEO) {
            if (mVideo != null && getMedia_image() != null) {
                mTypeOutput.addFormDataPart("Image", "image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"), getMedia_image()));
                mTypeOutput.addFormDataPart("Video", "video_upload.mp4", RequestBody.create(okhttp3.MediaType.parse("video/mp4"), getMedia_video()));
//                mTypeOutput.addPart("Image", new TypedFile("image/png", getMedia_image()));
//                mTypeOutput.addPart("Video", new TypedFile("video/mp4", getMedia_video()));
            }
        }
    }

    public int getMedia_type() {
        return mMediaType;
    }

    public void setMedia_type(int media_type) {
        this.mMediaType = media_type;
    }

    public File getMedia_video() {
        return mVideo;
    }

    public void setMedia_video(File media_video) {
        this.mVideo = media_video;
    }

    public File getMedia_image() {
        return mImage;
    }

    public void setMedia_image(File media_image) {
        this.mImage = media_image;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTaggedUsers() {
        return mTaggedUsers;
    }

    public void setTaggedUsers(String taggedUsers) {
        mTaggedUsers = taggedUsers;
        addPartNotEmptyString("TagUsers", taggedUsers);
    }
}

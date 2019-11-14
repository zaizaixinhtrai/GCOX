package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.common.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by User on 9/18/2015.
 */
public class CreateChallengeRequestModel extends TypeOutputModel {
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

    private String startedAt;
    private String endedAt;
    private String description;
    private String prizes;
    private int maxSubmission;


    public CreateChallengeRequestModel(String address,
                                       double latitude,
                                       double longitude,
                                       String title,
                                       int media_type,
                                       File media_image,
                                       String startedAt,
                                       String endedAt,
                                       String description,
                                       String prizes,
                                       int maxSubmission) {
        this.mAddress = address;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mTitle = title;
        this.mMediaType = media_type;
        this.mImage = media_image;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.description = description;
        this.prizes = prizes;
        this.maxSubmission = maxSubmission;
        handleAddMultiPar(media_type);
    }


    private void handleAddMultiPar(int type) {
        addPartNotEmptyString("Address", getAddress());
        addPartNotEmptyString("Latitude", String.valueOf(getLatitude()));
        addPartNotEmptyString("Longitude", String.valueOf(getLongitude()));
        addPartNotEmptyString("Title", getTitle());
        addPartNotEmptyString("EndedAt", this.endedAt);
        addPartNotEmptyString("StartedAt", this.startedAt);
        addPartNotEmptyString("Description", this.description);
        addPartNotEmptyString("PrizeText", this.prizes);
        addPartNotEmptyString("MaxSubmission", String.valueOf(this.maxSubmission));
        addPartNotEmptyString("MediaType", String.valueOf(this.mMediaType));

        handleAddFileData(type);
    }

    private void handleAddFileData(int type) {

//        if (type == Constants.POST_TYPE_QUOTES || type == Constants.POST_TYPE_IMAGE) {

        if (mImage != null) {
            mTypeOutput.addFormDataPart("Image", "image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"), getMedia_image()));
        }
//        }

//        if (type == Constants.POST_TYPE_VIDEO) {
//            if (mVideo != null) {
//                mTypeOutput.addFormDataPart("Image", "image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"), getMedia_image()));
//                mTypeOutput.addFormDataPart("Video", "video_upload.mp4", RequestBody.create(okhttp3.MediaType.parse("video/mp4"), getMedia_video()));
//            }
//        }
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

package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.util.StringUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by User on 9/29/2015.
 */
public class EditProfileRequestModel extends TypeOutputModel {
    @SerializedName("Email")
    @Expose
    private String mEmail;

    @SerializedName("DOB")
    @Expose
    private String mDOB;

    @SerializedName("DisplayName")
    @Expose
    private String mDisplayName;

    @SerializedName("Gender")
    @Expose
    private String mGender;

    @SerializedName("RefId")
    @Expose
    private String mRefId;

    @SerializedName("Image")
    @Expose
    private File mImage;

    @SerializedName("Nationality")
    @Expose
    private String mNationality;

    @SerializedName("About")
    @Expose
    private String mAbout;

    @SerializedName("DeviceUdid")
    @Expose
    private String mDevicesUDID;

    private String mOldPassword;
    private String mPassword;

    public EditProfileRequestModel(
            String gender,
            String display_name,
            String dob,
            String email,
            String nationality,
            String old_password,
            String password,
            File image,
            String ref_id,
            String About,
            String deviceUdid) {
        this.mGender = gender;
        this.mDisplayName = display_name;
        this.mDOB = dob;
        this.mEmail = email;
        this.mNationality = nationality;
        this.mOldPassword = old_password;
        this.mPassword = password;
        this.mImage = image;
        this.mRefId = ref_id;
        this.mAbout = About;
        this.mDevicesUDID = deviceUdid;

        handleAddPartData();
    }

    private void handleAddPartData() {
        if (mImage != null) {
//            TypedFile fileUpload = new TypedFile("image/png", getImage());
//            mTypeOutput.addPart("mImage", fileUpload);
            mTypeOutput.addFormDataPart("Image", "image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"), getImage()));
        }
        addPartNotEmptyString("DisplayName", mDisplayName);
        addPartNotEmptyString("DOB", mDOB);
        addPartNotEmptyString("Email", mEmail);
        addPartNotEmptyString("Gender", mGender);
        addPartNotEmptyString("Nationality", mNationality);
        addPartNotEmptyString("password", mPassword);
        addPartNotEmptyString("old_password", mOldPassword);
        if (!StringUtil.isNullOrEmptyString(mRefId)) addPartNotEmptyString("RefId", mRefId);
        addPartNotEmptyString("About", mAbout);
        addPartNotEmptyString("DeviceUdid", mDevicesUDID);

    }

    public String getAbout() {
        return mAbout;
    }

    public void setAbout(String about) {
        mAbout = about;
    }

    public String getRef_id() {
        return mRefId;
    }

    public void setRef_id(String ref_id) {
        this.mRefId = ref_id;
    }

    public File getImage() {
        return mImage;
    }

    public void setImage(File image) {
        this.mImage = image;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getDisplay_name() {
        return mDisplayName;
    }

    public void setDisplay_name(String display_name) {
        this.mDisplayName = display_name;
    }

    public String getDob() {
        return mDOB;
    }

    public void setDob(String dob) {
        this.mDOB = dob;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getNationality() {
        return mNationality;
    }

    public void setNationality(String nationality) {
        this.mNationality = nationality;
    }

    public String getOldPassword() {
        return mOldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.mOldPassword = oldPassword;
    }
}

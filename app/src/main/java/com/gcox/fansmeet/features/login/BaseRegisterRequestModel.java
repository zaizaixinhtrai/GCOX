package com.gcox.fansmeet.features.login;

import com.gcox.fansmeet.webservice.request.TypeOutputModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import okhttp3.RequestBody;

import java.io.File;

/**
 * Created by linh on 02/11/2016.
 */

public class BaseRegisterRequestModel extends TypeOutputModel {
    @SerializedName("UserName") @Expose
    private String mUserName;
    @SerializedName("DisplayName") @Expose
    private String mDisplayName;
    @SerializedName("DeviceType") @Expose
    private int mDeviceType;
    @SerializedName("DeviceUdid") @Expose
    private String mDeviceUdid;
    @SerializedName("DeviceToken") @Expose
    private String mDeviceToken;
    @SerializedName("Latitude") @Expose
    private double mLatitude;
    @SerializedName("Longitude") @Expose
    private double mLongitude;
    @SerializedName("Address") @Expose
    private String mAddress;
    @SerializedName("Email") @Expose
    private String mEmail;
    @SerializedName("Profile") @Expose
    private File mProfile;
    @SerializedName("RefId") @Expose
    private String mRefId;
    @SerializedName("Gender") @Expose
    private String mGender;
    @SerializedName("OSVersion") @Expose
    private String mOSVersion;
    @SerializedName("DeviceName") @Expose
    private String mDeviceName;
    @SerializedName("Version") @Expose
    private String mVersion;

    public void setOSVersion(String OSVersion) {
        this.mOSVersion = OSVersion;
    }

    public void setDeviceName(String deviceName) {
        this.mDeviceName = deviceName;
    }

    public void setVersion(String version) {
        this.mVersion = version;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getRef_id() {
        return mRefId;
    }

    public void setRef_id(String ref_id) {
        this.mRefId = ref_id;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public File getProfile() {
        return mProfile;
    }

    public void setProfile(File profile) {
        this.mProfile = profile;
    }

    public String getUsername() {
        return mUserName;
    }

    public void setUsername(String username) {
        this.mUserName = username;
    }

    public String getDisplayname() {
        return mDisplayName;
    }

    public void setDisplayname(String displayname) {
        this.mDisplayName = displayname;
    }

    public int getDevice_type() {
        return mDeviceType;
    }

    public void setDevice_type(int device_type) {
        this.mDeviceType = device_type;
    }

    public String getDevice_udid() {
        return mDeviceUdid;
    }

    public void setDevice_udid(String device_udid) {
        this.mDeviceUdid = device_udid;
    }

    public String getDevice_token() {
        return mDeviceToken;
    }

    public void setDevice_token(String device_token) {
        this.mDeviceToken = device_token;
    }


    public String getOSVersion() {
        return mOSVersion;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public String getVersion() {
        return mVersion;
    }

    public BaseRegisterRequestModel(String username,
                                    String displayname,
                                    int device_type,
                                    String device_udid,
                                    String device_token,
                                    double latitude,
                                    double longitude,
                                    String address,
                                    String email,
                                    File profile,
                                    String ref_id,
                                    String Gender,
                                    String deviceName,
                                    String OSVersion,
                                    String version) {
        this.mUserName = username;
        this.mDisplayName = displayname;
        this.mDeviceType = device_type;
        this.mDeviceUdid = device_udid;
        this.mDeviceToken = device_token;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAddress = address;
        this.mEmail = email;
        this.mProfile = profile;
        this.mRefId = ref_id;
        this.mGender = Gender;
        this.mDeviceName = deviceName;
        this.mOSVersion = OSVersion;
        this.mVersion = version;
    }

    protected void handleAddPartData() {
        if (mProfile != null) {
//            TypedFile fileUpload = new TypedFile("image/png", getProfile());
            mTypeOutput.addFormDataPart("Profile","image_upload.png", RequestBody.create(okhttp3.MediaType.parse("image/png"),getProfile()));
//            mTypeOutput.addPart("mProfile", fileUpload);
        }
        addPartNotEmptyString("UserName", getUsername());
        addPartNotEmptyString("DisplayName", getDisplayname());
        addPartNotEmptyString("DeviceType", String.valueOf(getDevice_type()));
        addPartNotEmptyString("DeviceUdid", getDevice_udid());
        addPartNotEmptyString("DeviceToken", getDevice_token());
        addPartNotEmptyString("Gender", getGender());
        addPartNotEmptyString("Latitude", String.valueOf(getLatitude()));
        addPartNotEmptyString("Longitude", String.valueOf(getLongitude()));
        addPartNotEmptyString("Email", getEmail());
        addPartNotEmptyString("RefId", getRef_id());
        addPartNotEmptyString("OSVersion", getOSVersion());
        addPartNotEmptyString("DeviceName", getDeviceName());
        addPartNotEmptyString("Version", getVersion());

    }

    @Override
    public String toString() {
        return "BaseRegisterRequestModel{" +
                "mUserName='" + mUserName + '\'' +
                ", mDisplayName='" + mDisplayName + '\'' +
                ", mDeviceType=" + mDeviceType +
                ", mDeviceUdid='" + mDeviceUdid + '\'' +
                ", mDeviceToken='" + mDeviceToken + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mAddress='" + mAddress + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mProfile=" + mProfile +
                ", mRefId='" + mRefId + '\'' +
                ", mGender='" + mGender + '\'' +
                ", mOSVersion='" + mOSVersion + '\'' +
                ", mDeviceName='" + mDeviceName + '\'' +
                ", mVersion='" + mVersion + '\'' +
                '}';
    }
}

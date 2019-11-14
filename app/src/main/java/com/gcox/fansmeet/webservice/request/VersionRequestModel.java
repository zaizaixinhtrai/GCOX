package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThanhBan on 10/3/2016.
 */

public class VersionRequestModel {
    @SerializedName("DeviceType")
    String mDeviceType;
    @SerializedName("Version")
    String mVersion;
    @SerializedName("DeviceUdid")
    String mDeviceUdid;
    @SerializedName("DeviceName")
    String mDeviceName;
    @SerializedName("OSVersion")
    String mOSVersion;
    @SerializedName("AppType")
    String mAppType;

    public VersionRequestModel(String deviceType, String version, String deviceUUID, String deviceName, String osVersion, String appType) {
        mDeviceType = deviceType;
        mVersion = version;
        mDeviceUdid =deviceUUID;
        mDeviceName =deviceName;
        mOSVersion =osVersion;
        mAppType = appType;
    }
}

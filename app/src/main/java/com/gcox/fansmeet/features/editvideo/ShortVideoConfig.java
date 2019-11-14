package com.gcox.fansmeet.features.editvideo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ngoc on 6/29/2017.
 */

public class ShortVideoConfig implements Parcelable {
    public int fps;
    public int resolution;
    public int videoBitrate;
    public int audioBitrate;
    public int encodeType;
    public int encodeMethod;
    public int encodeProfile;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fps);
        dest.writeInt(this.resolution);
        dest.writeInt(this.videoBitrate);
        dest.writeInt(this.audioBitrate);
        dest.writeInt(this.encodeType);
        dest.writeInt(this.encodeMethod);
        dest.writeInt(this.encodeProfile);
    }

    public ShortVideoConfig() {
    }

    protected ShortVideoConfig(Parcel in) {
        this.fps = in.readInt();
        this.resolution = in.readInt();
        this.videoBitrate = in.readInt();
        this.audioBitrate = in.readInt();
        this.encodeType = in.readInt();
        this.encodeMethod = in.readInt();
        this.encodeProfile = in.readInt();
    }

    public static final Creator<ShortVideoConfig> CREATOR = new Creator<ShortVideoConfig>() {
        @Override
        public ShortVideoConfig createFromParcel(Parcel source) {
            return new ShortVideoConfig(source);
        }

        @Override
        public ShortVideoConfig[] newArray(int size) {
            return new ShortVideoConfig[size];
        }
    };
}

package com.gcox.fansmeet.features.post;

import android.os.Parcel;
import android.os.Parcelable;
import com.gcox.fansmeet.common.BaseBundle;
import com.gcox.fansmeet.features.profile.ItemModelClassNewsFeed;

/**
 * Created by Ngoc on 16/10/2015.
 */
public class BundleMedia extends BaseBundle {

    private boolean isPost = false;
    private int type;
    private String postId;
    private String discription;
    private int position;
    private String uriPath;
    private ItemModelClassNewsFeed itemModelClassNewsFeed;
    public boolean isSubmissionChallenge;
    public int challengeId;

    public ItemModelClassNewsFeed getItemModelClassNewsFeed() {
        return itemModelClassNewsFeed;
    }

    public void setItemModelClassNewsFeed(ItemModelClassNewsFeed itemModelClassNewsFeed) {
        this.itemModelClassNewsFeed = itemModelClassNewsFeed;
    }

    public void setPost(boolean post) {
        isPost = post;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setIsPost(boolean isPost) {
        this.isPost = isPost;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    //Uri uri;
    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public BundleMedia() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isPost ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeString(this.postId);
        dest.writeString(this.discription);
        dest.writeInt(this.position);
        dest.writeString(this.uriPath);
        dest.writeParcelable(this.itemModelClassNewsFeed, flags);
        dest.writeByte(this.isSubmissionChallenge ? (byte) 1 : (byte) 0);
        dest.writeInt(this.challengeId);
    }

    protected BundleMedia(Parcel in) {
        super(in);
        this.isPost = in.readByte() != 0;
        this.type = in.readInt();
        this.postId = in.readString();
        this.discription = in.readString();
        this.position = in.readInt();
        this.uriPath = in.readString();
        this.itemModelClassNewsFeed = in.readParcelable(ItemModelClassNewsFeed.class.getClassLoader());
        this.isSubmissionChallenge = in.readByte() != 0;
        this.challengeId = in.readInt();
    }

    public static final Parcelable.Creator<BundleMedia> CREATOR = new Parcelable.Creator<BundleMedia>() {
        @Override
        public BundleMedia createFromParcel(Parcel source) {
            return new BundleMedia(source);
        }

        @Override
        public BundleMedia[] newArray(int size) {
            return new BundleMedia[size];
        }
    };
}

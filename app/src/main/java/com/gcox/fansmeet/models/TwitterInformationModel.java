package com.gcox.fansmeet.models;

/**
 * Created by User on 12/7/2016.
 */

public class TwitterInformationModel {

    private String twitterId;
    private String twitterEmail;
    private String twitterUsername;
    private String twitterImage;
    private String twitterDisplayName;

    public String getTwitterDisplayName() {
        return twitterDisplayName;
    }

    public void setTwitterDisplayName(String twitterDisplayName) {
        this.twitterDisplayName = twitterDisplayName;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getTwitterEmail() {
        return twitterEmail;
    }

    public void setTwitterEmail(String twitterEmail) {
        this.twitterEmail = twitterEmail;
    }

    public String getTwitterUsername() {
        return twitterUsername;
    }

    public void setTwitterUsername(String twitterUsername) {
        this.twitterUsername = twitterUsername;
    }

    public String getTwitterImage() {
        return twitterImage;
    }

    public void setTwitterImage(String twitterImage) {
        this.twitterImage = twitterImage;
    }
}

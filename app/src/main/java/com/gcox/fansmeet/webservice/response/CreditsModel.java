package com.gcox.fansmeet.webservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/16/2015.
 */
public class CreditsModel {
    @SerializedName("Gems")
    @Expose
    private long gems;
    @SerializedName("Stars")
    @Expose
    private long stars;
    @SerializedName("Loyalty")
    @Expose
    private long loyalty;

    public long getGems() {
        return gems;
    }

    public long getStars() {
        return stars;
    }

    public long getLoyalty() {
        return loyalty;
    }
}

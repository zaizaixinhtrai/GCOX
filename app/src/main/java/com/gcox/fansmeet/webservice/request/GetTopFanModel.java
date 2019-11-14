package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 8/30/2016.
 */
public class GetTopFanModel extends BasePagingRequestModel{
    @SerializedName("UserId")
    private String mUserId;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public Map<String,String> getMappedRequest(){
        Map<String,String> map = new HashMap<>();
        map.put("UserId",getUserId());
        map.put("NextId", String.valueOf(getNextId()));
        map.put("Limit", String.valueOf(getLimit()));
        return map;
    }
}

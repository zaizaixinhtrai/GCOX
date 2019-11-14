package com.gcox.fansmeet.webservice.request;

import okhttp3.MultipartBody;


/**
 * Created by User on 9/12/2015.
 */
public class TypeOutputModel {
    protected MultipartBody.Builder mTypeOutput = new MultipartBody.Builder()
            .setType(MultipartBody.FORM);

    public void addPartNotEmptyString(String key,String value){
        if(value!=null && !value.isEmpty()){
            mTypeOutput.addFormDataPart(key,value);
        }
    }

    public MultipartBody build(){
        return mTypeOutput.build();
    }
}

package com.gcox.fansmeet.features.refill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefillListItem {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("AndroidStoreItemId")
    @Expose
    private String StoreId;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("PriceCny")
    @Expose
    private String PriceCny;
    @SerializedName("Percentage")
    @Expose
    private int Percentage;
    @SerializedName("Gem")
    @Expose
    private int gem;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public int getBean() {
        return gem;
    }

    public void setBean(int bean) {
        this.gem = bean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAndroid_store_id() {
        return StoreId;
    }

    public void setAndroid_store_id(String android_store_id) {
        this.StoreId = android_store_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice_usd() {
        return price;
    }

    public void setPrice_usd(String price_usd) {
        this.price = price_usd;
    }

    public String getPrice_cny() {
        return PriceCny;
    }

    public void setPrice_cny(String price_cny) {
        this.PriceCny = price_cny;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        this.Percentage = percentage;
    }

    public String getIos_store_id() {
        return StoreId;
    }

    public void setIos_store_id(String ios_store_id) {
        this.StoreId = ios_store_id;
    }
}

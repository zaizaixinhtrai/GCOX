package com.gcox.fansmeet.billing;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.SkuDetails;

public class SkuRowData {
    private String sku, title, price, description;
    private @BillingClient.SkuType
    String billingType;
    private String originalJson;

    public SkuRowData(SkuDetails details,
                      @BillingClient.SkuType String billingType) {
        this.sku = details.getSku();
        this.title = details.getTitle();
        this.price = details.getPrice();
        this.description = details.getDescription();
        this.billingType = billingType;
        this.originalJson = details.getOriginalJson();
    }

    public String getSku() {
        return sku;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public @BillingClient.SkuType
    String getSkuType() {
        return billingType;
    }

    public String getOriginalJson() {
        return originalJson;
    }
}

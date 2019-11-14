package com.gcox.fansmeet.util;

import android.content.Context;
import android.text.TextUtils;
import com.gcox.fansmeet.common.Constants;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;

/**
 * Created by linh on 18/05/2017.
 */

public class BranchIoUtil {

    public static void generateBranchIoUrl(Context context, String image, String referralId, String chanel, BranchIoUtil.OnBranchIoCallback callback) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
//                .setCanonicalIdentifier("item/12345")
//                .setTitle("My Content Title")
//                .setContentDescription("My Content Description")
                .setContentImageUrl(image)
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
//                .addContentMetadata(Constants.REFERRAL_ID, referralId);
                .setContentMetadata(new ContentMetadata().addCustomMetadata(Constants.REFERRAL_ID, referralId));

//        branchUniversalObject.userCompletedAction(BranchEvent.VIEW);
//        branchUniversalObject.userCompletedAction(BranchEvent.SHARE_COMPLETED);

        branchUniversalObject.userCompletedAction(BRANCH_STANDARD_EVENT.VIEW_ITEMS);
        branchUniversalObject.userCompletedAction(BRANCH_STANDARD_EVENT.VIEW_ITEM);
        branchUniversalObject.userCompletedAction(BRANCH_STANDARD_EVENT.SHARE);

        LinkProperties linkProperties = new LinkProperties()
                .setChannel(chanel)
                .setCampaign("sharing");

        branchUniversalObject.generateShortUrl(context, linkProperties, (url, error) -> {
            if (error == null && !TextUtils.isEmpty(url)) {
                callback.onComplete(url);
            }
        });
    }

    public static void generateBranchIoUrl(Context context, String image, String referralId, BranchIoUtil.OnBranchIoCallback callback) {
        generateBranchIoUrl(context, image, referralId, "Belive", callback);
    }

    public interface OnBranchIoCallback {
        void onComplete(String url);
    }
}

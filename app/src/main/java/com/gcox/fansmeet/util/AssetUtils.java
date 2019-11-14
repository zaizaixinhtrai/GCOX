package com.gcox.fansmeet.util;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thanhbc on 6/28/17.
 */

public class AssetUtils {
    public static List<String> getJsonAssets(Context context, String path) throws IOException {
        String[] assetList = context.getAssets().list(path);
        List<String> files = new ArrayList<>();
        for (String asset : assetList) {
            if (asset.toLowerCase().endsWith(".json")) {
                files.add(asset);
            }
        }
        return files;
    }
}

package com.gcox.fansmeet.features.setting;

import com.gcox.fansmeet.features.mvpbase.BaseContract;

/**
 * Created by linh on 12/12/2016.
 */

public interface SettingContract {
    interface SettingActivityView extends BaseContract.View{
        void showProgress(String message);
        void onSettingUpdateSuccessfully();
        void onHasNewUpdates(boolean hasNewUpdates);
        void onCheckUpdateFailed(String message);
    }

    interface UserActions extends BaseContract.Presenter<SettingActivityView>{
        void checkUpdates();
    }
}

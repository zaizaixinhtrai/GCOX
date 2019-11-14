package com.gcox.fansmeet.features.blocked_screen;


import com.gcox.fansmeet.features.mvpbase.BaseContract;

import java.util.List;

/**
 * Created by ThanhBan on 9/26/2016.
 */

interface BlockedScreenContract {
    interface BlockedUserView extends BaseContract.View {
        void onBlockedListResponse(List<BlockedUserModel> likedUsers);
        void onUnblockUserSuccessfully(int position);
    }

    interface UserActions extends BaseContract.Presenter<BlockedUserView> {
    }
}

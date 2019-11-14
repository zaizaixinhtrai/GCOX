package com.gcox.fansmeet.customview.hashtag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.util.Utils;
import io.realm.RealmResults;


/**
 * Created by linh on 19/06/2017.
 */

public class UserTagListPopUp extends PopupWindow {

    @Bind(R.id.realm_rcv_taggable_user)
    RealmRecyclerView rv;
    RealmTagListAdapter mAdapter;

    public static UserTagListPopUp newInstance(Context context, RealmResults<FollowItem> items, RealmTagListAdapter.ItemClickListener clickListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup_taggable_user_list, null);
        int width = Utils.getScreenWidth() - Utils.dpToPx(24);
        UserTagListPopUp popupWindow = new UserTagListPopUp(
                customView,
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT, items, clickListener
        );
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return popupWindow;
    }

    private UserTagListPopUp(View contentView, int width, int height, RealmResults<FollowItem> items, RealmTagListAdapter.ItemClickListener clickListener) {
        super(contentView, width, height);
        ButterKnife.bind(this, contentView);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setupRecyclerView(contentView.getContext(), items, clickListener);
        setBackgroundDrawable(new ColorDrawable(Color.BLUE));
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void unbind(){
        ButterKnife.unbind(this);
    }

    public void updateLocation(View anchor, int x, int y){
        update(anchor, x, y, -1, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    public void updateList(RealmResults<FollowItem> realmResults, String query){
        mAdapter.setQuery(query);
        mAdapter.updateRealmResults(realmResults);
    }

    private void setupRecyclerView(Context context, RealmResults<FollowItem> items, RealmTagListAdapter.ItemClickListener clickListener){
        mAdapter = new RealmTagListAdapter(context, items, true, true, clickListener);
        rv.setAdapter(mAdapter);
    }
}
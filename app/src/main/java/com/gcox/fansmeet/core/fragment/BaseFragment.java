package com.gcox.fansmeet.core.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.activity.BaseActivity;
import com.gcox.fansmeet.core.activity.HandleErrorActivity;
import com.gcox.fansmeet.core.dialog.ProgressHUD;

/**
 * Created by USER on 10/8/2015.
 */

public class BaseFragment extends Fragment {
    protected View mRootView;
    protected OnFragmentInteractionListener mListener;
    protected Bundle mArgument;
    ProgressHUD dialog;
    String dialogMessage = "";
    public boolean areLecturesLoaded = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialogLoading(getActivity(), dialogMessage);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListener != null) {
            mListener.onFragmentAttachToActivity(this);
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = AppsterApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);

        if (dialog != null && dialog.isShowing()) {
            dismissDialog();
        }
        dialog = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentAttachToActivity(BaseFragment fragment);
    }

    protected Bundle getBundleData() {
        return mArgument;
    }

    public void setData(Bundle args) {
        mArgument = args;
    }


    public void createDialogLoading(Context mcContext, String message) {
        dialog = new ProgressHUD(mcContext,
                R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hudd);
//        if (message == null || message.length() == 0) {
//            dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);
//        } else {
//            TextView txt = (TextView) dialog.findViewById(R.id.message);
//            txt.setText(message);
//        }
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);

    }

    protected void showDialog() {
        if (isFragmentUIActive()) {
            if (dialog != null) {
                dialog.show();
            } else {
                createDialogLoading(getContext(), "");
            }
        }
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    public void onErrorWebServiceCall(String errorMessage, int errorCode) {

        Activity activity = getActivity();

        if (activity == null)
            return;

        if (!activity.isFinishing() && !activity.isDestroyed() && activity instanceof HandleErrorActivity) {
            ((HandleErrorActivity) getActivity()).handleError(errorMessage, errorCode);
        }
    }

    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }

    public void scrollTopUpRecyclerView(RecyclerView recyclerView, boolean isSmoothScroll) {
        if (isFragmentUIActive() && recyclerView != null) {
//            recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));
            if (isSmoothScroll) {
                recyclerView.smoothScrollToPosition(0);
            } else {
                recyclerView.getLayoutManager().scrollToPosition(0);
            }
        }
    }


}

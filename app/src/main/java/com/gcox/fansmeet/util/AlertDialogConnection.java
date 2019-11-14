package com.gcox.fansmeet.util;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import java.util.concurrent.TimeUnit;

import com.gcox.fansmeet.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by ThanhBan on 10/18/2016.
 */

public class AlertDialogConnection extends DialogFragment {

    public static final String ALERT_FRAGMENT_TAG = "AlertFragment";
    Button btnOK;

    TextView tvDialogContent;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog1 = new Dialog(getActivity());
        dialog1.setCancelable(false);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.dialog_confirmation);

        TextView tvDialogTitle = (TextView) dialog1.findViewById(R.id.dialog_title);
        tvDialogContent = (TextView) dialog1.findViewById(R.id.dialog_message);
        Button btnCancel = (Button) dialog1.findViewById(R.id.cancel);
        btnCancel.setVisibility(View.GONE);
         btnOK = (Button) dialog1.findViewById(R.id.ok);
        btnOK.setText("Re-connecting");
        tvDialogTitle.setText("Connection lost!");

        tvDialogContent.setText(getString(R.string.no_internet_connection));
        return dialog1;
    }
//    private void openSetting() {
//        Intent settingIntent = new Intent();
//        settingIntent.setAction(Settings.ACTION_SETTINGS);
//        startActivity(settingIntent);
//    }

    public void setBtnReconnectClick(@NonNull OnConnectionDialogClickListener connectionDialogClickListener){

        Observable.just(true).delay(10, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            if(isFragmentUIActive()){
                tvDialogContent.setText("Sorry ;( \n Reconnect was not successfully.");
                btnOK.setText(R.string.btn_text_ok);
                btnOK.setOnClickListener(view -> connectionDialogClickListener.onConnectionDialogClicked());
            }
        },error-> Timber.e(error.getMessage()));
//        btnOK.setText(text);
//        btnOK.setOnClickListener(view -> connectionDialogClickListener.onConnectionDialogClicked());
    }

    public boolean isFragmentUIActive() {
        return isAdded() && !isDetached() && !isRemoving();
    }

  public  interface OnConnectionDialogClickListener{
        void onConnectionDialogClicked();
    }
}

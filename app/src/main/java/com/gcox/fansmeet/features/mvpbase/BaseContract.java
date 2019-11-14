package com.gcox.fansmeet.features.mvpbase;

import android.content.Context;

/**
 * Created by ThanhBan on 9/14/2016.
 */
public interface BaseContract {

  interface Presenter<V> {
      void attachView(V view);
      void detachView();
  }

    interface View{
        Context getViewContext();
        void loadError(String errorMessage, int code);
        void showProgress();
        void hideProgress();
    }
}

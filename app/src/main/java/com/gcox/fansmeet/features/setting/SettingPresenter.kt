package com.gcox.fansmeet.features.setting

import android.content.Context
import android.content.pm.PackageManager
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.BuildConfig
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.models.UserModel
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.min

/**
 * Created by linh on 12/12/2016.
 */

open class SettingPresenter(view: SettingContract.SettingActivityView, mService: AppsterWebserviceAPI) :
    SettingContract.UserActions {
    private var context: Context? = null
    private var view: SettingContract.SettingActivityView? = null
    private val mService: AppsterWebserviceAPI? = null
    private val authen: String? = null
    protected val compositeDisposable by lazy { CompositeDisposable() }

    init {
        attachView(view)
        context = view as Context

    }

    override fun attachView(view: SettingContract.SettingActivityView) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
        context = null
    }

    override fun checkUpdates() {
        compositeDisposable.add(
            AppsterWebServices.get().checkVersion(
                Constants.ANDROID_DEVICE_TYPE,
                BuildConfig.VERSION_CODE.toString()
            )
                .filter { view != null && context != null }
                .subscribe({ versionDataResponse ->
                    if(versionDataResponse.code != Constants.RESPONSE_FROM_WEB_SERVICE_OK){
                        view?.loadError(versionDataResponse.message,versionDataResponse.code)
                    }else {
                        try {
                            val appConfig = versionDataResponse.data
                            val newestVersion = appConfig.androidVersion
                            val currentVersion = AppsterApplication.getCurrentVersionName(context!!)
                            val compareResult = compareVersionNames(currentVersion, newestVersion)
                            view?.onHasNewUpdates(compareResult == -1)
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }
                    }

                },{ error -> view?.loadError(error.message,Constants.RETROFIT_ERROR)})
        )
    }

     fun updateSetting(userInforModel: UserModel) {


    }

     fun confirmDeactivateAccount() {


    }

     fun deactivateAccount() {


    }

     fun saveUserInfo(userInfo: UserModel) {

    }

    /**
     * @return 1 if the #oldVersionName is greater than @newVersionName
     * 0 if the @{link oldVersionName} and newVersionName are equal
     * otherwise return -1
     */
    fun compareVersionNames(oldVersionName: String, newVersionName: String): Int {
        var res = 0

        val oldNumbers =
            oldVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val newNumbers =
            newVersionName.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // To avoid IndexOutOfBounds
        val maxIndex = min(oldNumbers.size, newNumbers.size)

        for (i in 0 until maxIndex) {
            val oldVersionPart = Integer.parseInt(oldNumbers[i])
            val newVersionPart = Integer.parseInt(newNumbers[i])

            if (oldVersionPart < newVersionPart) {
                res = -1
                break
            } else if (oldVersionPart > newVersionPart) {
                res = 1
                break
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.size != newNumbers.size) {
            res = if (oldNumbers.size > newNumbers.size) 1 else -1
        }

        return res
    }


    //    private void confirmLogout() {
    //
    //        DialogYesNo confirmDel = new DialogYesNo(SettingActivity.this);
    //        confirmDel.showDialog(getString(
    //                R.string.are_you_sure_you_want_to_logout));
    //
    //        confirmDel.setOnclickConfirm(new DialogYesNo.OnclickConfirm() {
    //            @Override
    //            public void onClickOk() {
    //
    //                if (CheckNetwork.isNetworkAvailable(SettingActivity.this)) {
    //                    logout();
    //                    AppsterApplication.logout();
    //                    finish();
    //
    //                } else {
    //
    //                    utility.showMessage(
    //                            getString(R.string.app_name),
    //                            getResources().getString(
    //                                    R.string.no_internet_connection),
    //                            SettingActivity.this);
    //                }
    //
    //            }
    //
    //            @Override
    //            public void onClickCancel() {
    //
    //            }
    //        });
    //
    //    }

    //    private void logout() {
    //        //  DialogManager.getInstance().showDialog(SettingActivity.this, getResources().getString(R.string.connecting_msg));
    //        showDialog(this, getResources().getString(R.string.connecting_msg));
    //        LogoutRequestModel request = new LogoutRequestModel();
    //        AppsterWebServices.get().logoutApp("Bearer " + AppsterApplication.mAppPreferences.getUserToken(), request,
    //                new Callback<BaseResponse<Boolean>>() {
    //                    @Override
    //                    public void success(LogoutDataResponse logoutResponseModel, Response response) {
    //                        // DialogManager.getInstance().dismisDialog();
    //                        dismisDialog();
    //
    //                        if (logoutResponseModel == null) return;
    //
    //                        if (logoutResponseModel.getCode() != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
    //                            handleError(logoutResponseModel.getMessage(), logoutResponseModel.getCode());
    //                        }
    //                        SocialManager.getInstance().logOut();
    //                        SocialManager.logoutGoogle(SettingActivity.this, null);
    //                    }
    //
    //                    @Override
    //                    public void failure(RetrofitError error) {
    //                        DialogManager.getInstance().dismisDialog();
    //
    //                        handleError(error.getMessage(), Constants.RETROFIT_ERROR);
    //                    }
    //                });
    //    }
}

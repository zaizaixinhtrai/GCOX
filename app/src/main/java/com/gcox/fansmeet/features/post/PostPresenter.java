package com.gcox.fansmeet.features.post;

import com.appster.features.mvpbase.BasePresenter;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.data.repository.PostDataRepository;
import com.gcox.fansmeet.data.repository.datasource.cloud.CloudPostDataSource;
import com.gcox.fansmeet.domain.interactors.post.CreatePostUseCase;
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Created by linh on 10/10/2017.
 */

public class PostPresenter extends BasePresenter<PostContract.View> implements PostContract.PostActions {
    protected final AppsterWebserviceAPI mService;
    protected final String mAuthen;
    private CreatePostUseCase mCreatePostUseCase;
    private CompositeDisposable compositeDisposable;

    public PostPresenter(AppsterWebserviceAPI service, String authen) {
        mService = service;
        mAuthen = authen;
        final Scheduler uiThread = AndroidSchedulers.mainThread();
        final Scheduler io = Schedulers.io();
        PostDataRepository postDataRepository = new PostDataRepository(new CloudPostDataSource(mService));
        mCreatePostUseCase = new CreatePostUseCase(uiThread, io, postDataRepository);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void post(boolean isEditing, boolean isSubmissionChallenge, int challengeId, MultipartBody multipartBody) {
        getView().showProgress();
        compositeDisposable.add(mCreatePostUseCase.execute(CreatePostUseCase.Params.load(isEditing, isSubmissionChallenge, challengeId, multipartBody))
                .subscribe(postCreatePostResponseModel -> {
                    if (postCreatePostResponseModel == null) return;

                    if (postCreatePostResponseModel.getCode() != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        if (getView() != null)
                            getView().loadError(postCreatePostResponseModel.getMessage(), postCreatePostResponseModel.getCode());
                        return;
                    }
                    if (getView() != null) {
                        getView().hideProgress();
                        getView().onPostSuccessfully(postCreatePostResponseModel.getData());
                    }
                }, error -> {
                    if (getView() != null) {
                        getView().hideProgress();
                        getView().loadError(error.getMessage(), Constants.RETROFIT_ERROR);
                    }
                }));
    }

    @Override
    public void detachView() {
        super.detachView();
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }
}
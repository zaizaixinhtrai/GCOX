package com.gcox.fansmeet.domain.repository;

import com.gcox.fansmeet.models.AppConfigModel;
import com.gcox.fansmeet.webservice.response.BaseResponse;
import io.reactivex.Observable;

/**
 * Created by linh on 21/12/2017.
 */

public interface AppConfigsRepository {
    Observable<BaseResponse<AppConfigModel>> getAppConfigs();
}

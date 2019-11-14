package com.gcox.fansmeet.features.rewards

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.main.IAPIsfinishedCheckingUseCase
import com.gcox.fansmeet.domain.interactors.main.VerifyPurchaseTransactionUseCase
import com.gcox.fansmeet.domain.interactors.reward.CheckUnredeemedUseCase
import com.gcox.fansmeet.domain.interactors.reward.GetPackagesUseCase
import com.gcox.fansmeet.domain.interactors.reward.RewardListUseCase
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import com.gcox.fansmeet.webservice.response.VerifyIAPResponeModel


/**
 * Created by Ngoc on 5/18/18.
 */
class RewardViewModel constructor(
    private val rewardListUseCase: RewardListUseCase,
    private val checkUnredeemedUseCase: CheckUnredeemedUseCase,
    private val getPackagesUseCase: GetPackagesUseCase
) : BaseViewModel() {

    val getRewardList = MutableLiveData<RewardListResponse>()
    val checkUnredeemed = MutableLiveData<List<PrizeCollectModel>>()
    val getPackages = MutableLiveData<CelebrityBoxesModel>()

    fun getRewardList(nextId: Int, limit: Int) {
        runUseCase(rewardListUseCase, RewardListUseCase.Params.load(nextId, limit)) {
            getRewardList.value = it
        }
    }

    fun checkUnredeemed() {
        runUseCase(checkUnredeemedUseCase, Unit) {
            checkUnredeemed.value = it
        }
    }

    fun getPackages(ownerId: Int) {
        runUseCase(getPackagesUseCase, ownerId) {
            getPackages.value = it
        }
    }

}

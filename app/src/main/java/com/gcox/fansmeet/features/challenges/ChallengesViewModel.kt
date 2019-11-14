package com.gcox.fansmeet.features.challenges

import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.mvvm.BaseViewModel
import com.gcox.fansmeet.domain.interactors.challenges.GetChallengeListEntriesUseCase
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel


/**
 * Created by Ngoc on 5/18/18.
 */
class ChallengesViewModel constructor(
    private val getChallengeListEntriesUseCase: GetChallengeListEntriesUseCase
) : BaseViewModel() {

    private var challengeListEntries = MutableLiveData<BaseDataPagingResponseModel<DisplayableItem>>()
    val getChallengeListEntries = challengeListEntries

    fun getChallengeList( nextId: Int,  pageLimited: Int){
        runUseCase(getChallengeListEntriesUseCase, GetChallengeListEntriesUseCase.Params.loadPage(nextId,pageLimited)) {
            challengeListEntries.value = it
        }
    }
}

package com.gcox.fansmeet.data.di

import com.gcox.fansmeet.data.di.NameInject.SCHEDULE_IO
import com.gcox.fansmeet.data.di.NameInject.SCHEDULE_UI
import com.gcox.fansmeet.data.repository.*
import com.gcox.fansmeet.data.repository.datasource.*
import com.gcox.fansmeet.data.repository.datasource.cloud.*
import com.gcox.fansmeet.data.repository.datasource.remote.CloudLoginDataSource
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityGridUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityListUseCase
import com.gcox.fansmeet.domain.interactors.celebrity.GetCelebrityProfileUseCase
import com.gcox.fansmeet.domain.interactors.challenges.*
import com.gcox.fansmeet.domain.interactors.challenhes.CanSubmitChallengeUseCase
import com.gcox.fansmeet.domain.interactors.challenhes.ViewContestantEntriesUseCase
import com.gcox.fansmeet.domain.interactors.comments.AddCommentUseCase
import com.gcox.fansmeet.domain.interactors.comments.DeleteCommentUseCase
import com.gcox.fansmeet.domain.interactors.comments.GetCommentsListUseCase
import com.gcox.fansmeet.domain.interactors.editprofile.CheckEmailUseCase
import com.gcox.fansmeet.domain.interactors.editprofile.EditProfileUseCase
import com.gcox.fansmeet.domain.interactors.follow.GetFollowListUseCase
import com.gcox.fansmeet.domain.interactors.home.GetBannersUseCase
import com.gcox.fansmeet.domain.interactors.home.GetUsersUseCase
import com.gcox.fansmeet.domain.interactors.login.FacebookLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.GoogleLoginUseCase
import com.gcox.fansmeet.domain.interactors.login.InstagramLoginUseCase
import com.gcox.fansmeet.domain.interactors.loyalty.LoyaltyUseCase
import com.gcox.fansmeet.domain.interactors.main.IAPIsfinishedCheckingUseCase
import com.gcox.fansmeet.domain.interactors.main.VerifyPurchaseTransactionUseCase
import com.gcox.fansmeet.domain.interactors.post.CreateChallengePostUseCase
import com.gcox.fansmeet.domain.interactors.post.DeleteChallengeUseCase
import com.gcox.fansmeet.domain.interactors.prize.BoxesListUseCase
import com.gcox.fansmeet.domain.interactors.prize.PrizeListUseCase
import com.gcox.fansmeet.domain.interactors.reward.CheckUnredeemedUseCase
import com.gcox.fansmeet.domain.interactors.reward.GetPackagesUseCase
import com.gcox.fansmeet.domain.interactors.reward.RewardListUseCase
import com.gcox.fansmeet.domain.interactors.stars.StartUseCase
import com.gcox.fansmeet.domain.interactors.topfans.TopFansUseCase
import com.gcox.fansmeet.domain.interactors.useraction.*
import com.gcox.fansmeet.domain.repository.*
import com.gcox.fansmeet.features.challengedetail.ChallengeDetailViewModel
import com.gcox.fansmeet.features.challengeentries.ChallengeEntriesViewModel
import com.gcox.fansmeet.features.challenges.ChallengesViewModel
import com.gcox.fansmeet.features.comment.CommentsViewModel
import com.gcox.fansmeet.features.editprofile.EditProfileViewModel
import com.gcox.fansmeet.features.flashscreen.SplashScreenViewModel
import com.gcox.fansmeet.features.home.HomeViewModel
import com.gcox.fansmeet.features.home.merchants.MerchantsViewModel
import com.gcox.fansmeet.features.login.LoginViewModel
import com.gcox.fansmeet.features.loyalty.LoyaltyViewModel
import com.gcox.fansmeet.features.main.MainViewModel
import com.gcox.fansmeet.features.prizelist.PrizeListActivityViewModel
import com.gcox.fansmeet.features.prizelist.PrizeListFragmentViewModel
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.*
import com.gcox.fansmeet.features.profile.userprofile.UserInfoViewModel
import com.gcox.fansmeet.features.profile.userprofile.UserProfileListViewModel
import com.gcox.fansmeet.features.rewards.RewardViewModel
import com.gcox.fansmeet.features.sendgift.SendGiftViewModel
import com.gcox.fansmeet.features.stars.StarsViewModel
import com.gcox.fansmeet.features.topfans.TopFansViewModel
import com.gcox.fansmeet.webservice.AppsterWebServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

object NameInject {
    const val SCHEDULE_UI = "ui"
    const val SCHEDULE_IO = "io"
}

val appModules = listOf(
    module {
        single(SCHEDULE_UI) {
            AndroidSchedulers.mainThread()
        }
        single(SCHEDULE_IO) {
            Schedulers.io()
        }

        single {
            AppsterWebServices.get()
        }

//        single {
//            AppsterUtility.getAuth()
//        }
    },
    // login modules
    module {
        factory<LoginDataSource> { CloudLoginDataSource(get()) }
        factory<UserLoginRepository> { UserLoginDataRepository(get()) }
        factory { FacebookLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GoogleLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { InstagramLoginUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { LoginViewModel(get(), get(), get()) }
    },

    // home modules
    module {
        factory<HomeDataSource> { CloudHomeDataSource(get()) }
        factory<HomeRepository> { HomeDataRepository(get()) }
        factory { GetUsersUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GetBannersUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { HomeViewModel(get(), get(), get(), get()) }
    },

    // merchants modules
    module {
        viewModel { MerchantsViewModel(get(), get(), get(), get()) }
    },

    // home celebrity
    module {
        factory<CelebrityDataSource> { CloudCelebrityDataSource(get()) }
        factory<CelebrityRepository> { CelebrityDataRepository(get()) }
        factory { GetCelebrityListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GetCelebrityProfileUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { CelebrityViewModel(get(), get()) }
    },

    // join challenge
    module {
        factory<ChallengeDataSource> { CloudChallengeDataSource(get()) }
        factory<ChallengeRepository> { ChallengeDataRepository(get()) }
        factory { GetChallengeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GetChallengeEntriesUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { ChallengeDetailViewModel(get(), get(), get(), get(), get()) }
    },

    // view Entries
    module {
        factory { ViewContestantEntriesUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { DeleteSubmissionUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { ChallengeEntriesViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    },

    // user action
    module {
        factory<UserActionDataSource> { CloudUserActionDataSource(get()) }
        factory<UserActionRepository> { UserActionDataRepository(get()) }
        factory { SendStartUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { FollowUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { LikeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { UnlikeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { UnFollowUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { BlockUserUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { ReportPostUserUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { ReportEntriesUserUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

        viewModel { SendGiftViewModel(get()) }
    },

    // top fans
    module {
        factory<TopFansDataSource> { CloudTopFansDataSource(get()) }
        factory<TopFansRepository> { TopFansDataRepository(get()) }
        factory { TopFansUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { TopFansViewModel(get()) }
    },

    // challenge list
    module {
        factory { GetChallengeListEntriesUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { ChallengesViewModel(get()) }
    },

    // stars
    module {
        factory<StarsDataSource> { CloudStarsDataSource(get()) }
        factory<StarsRepository> { StarsDataRepository(get()) }
        factory { StartUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { StarsViewModel(get()) }
    },

    // stars
    module {
        factory<LoyaltyDataSource> { CloudLoyaltyDataSource(get()) }
        factory<LoyaltyRepository> { LoyaltyDataRepository(get()) }
        factory { LoyaltyUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { LoyaltyViewModel(get()) }
    },

    // user profile list
    module {
        factory { CanSubmitChallengeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { UserProfileListViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    },

    // user info
    module {
        viewModel { UserInfoViewModel(get(), get(), get(), get()) }
    },

    // user profile grid
    module {
        factory { GetCelebrityGridUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { UserProfileGridViewModel(get()) }
    },

    // post challenge
    module {
        factory<PostDataSource> { CloudPostDataSource(get()) }
        factory<PostRepository> { PostDataRepository(get()) }
        factory { CreateChallengePostUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { DeleteChallengeUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

        viewModel { PostChallengeViewModel(get()) }
    },

    // edit profile
    module {
        factory<EditProfileDataSource> { CloudEditProfileDataSource(get()) }
        factory<EditProfileRepository> { EditProfileDataRepository(get()) }
        factory { EditProfileUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { CheckEmailUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { EditProfileViewModel(get(), get()) }
    },

    // follow
    module {
        factory<FollowDataSource> { CloudFollowDataSource(get()) }
        factory<FollowRepository> { FollowDataRepository(get()) }
        factory { GetFollowListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { FollowViewModel(get(), get(), get()) }
    },

    // comments
    module {
        factory<CommentsDataSource> { CloudCommentDataSource(get()) }
        factory<CommentsRepository> { CommentsDataRepository(get()) }
        factory { GetCommentsListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { AddCommentUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { DeleteCommentUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { CommentsViewModel(get(), get(), get()) }
    },
    // Splash modules
    module {
        viewModel { SplashScreenViewModel(get(), get(), get()) }
    },

    // Splash modules
    module {
        factory<MainDataSource> { CloudMainDataSource(get()) }
        factory<MainRepository> { MainDataRepository(get()) }
        factory { IAPIsfinishedCheckingUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { VerifyPurchaseTransactionUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

        viewModel { MainViewModel(get(), get()) }

    },

    // Rewards modules
    module {
        factory<RewardDataSource> { CloudRewardDataSource(get()) }
        factory<RewardRepository> { RewardDataRepository(get()) }
        factory { CheckUnredeemedUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { RewardListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        factory { GetPackagesUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }

        viewModel { RewardViewModel(get(),get(),get()) }

    },

    // Prize List modules
    module {
        factory { PrizeListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { PrizeListFragmentViewModel(get()) }

    },

    module {
        factory { BoxesListUseCase(get(SCHEDULE_UI), get(SCHEDULE_IO), get()) }
        viewModel { PrizeListActivityViewModel(get()) }

    }
)
package com.gcox.fansmeet.webservice;

import com.gcox.fansmeet.customview.hashtag.FollowItem;
import com.gcox.fansmeet.data.entity.*;
import com.gcox.fansmeet.features.blocked_screen.BlockedUserModel;
import com.gcox.fansmeet.features.notification.NotificationItemModel;
import com.gcox.fansmeet.features.refill.GemResponse;
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel;
import com.gcox.fansmeet.features.rewards.models.UsePointsResponsModel;
import com.gcox.fansmeet.features.topfans.TopFanModel;
import com.gcox.fansmeet.models.PostDataModel;
import com.gcox.fansmeet.webservice.request.*;
import com.gcox.fansmeet.webservice.response.*;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.*;

import java.util.List;

/**
 * Created by User on 9/8/2015.
 */
public interface AppsterWebserviceAPI {

    @POST("api/users/LoginWithGoogle")
    Observable<BaseResponse<LoginResponseModel>> loginWithGoogle(@Body GoogleLoginRequestModel request);

    @POST("api/users/LoginWithFacebook")
    Observable<BaseResponse<LoginResponseModel>> loginWithFacebook(@Body LoginFacebookRequestModel request);

    @POST("api/users/LoginWithInstagram")
    Observable<BaseResponse<LoginResponseModel>> loginWithInstagram(@Body InstagramLoginRequestModel request);

    @GET("api/settings/home/banners")
    Observable<BaseResponse<List<String>>> homeBanners(@Header("Authorization") String connection);

    @GET("api/users/celebrities")
    Observable<BaseResponse<UsersEntity>> homeCelebrities(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/events")
    Observable<BaseResponse<UsersEntity>> homeEvents(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/influencers")
    Observable<BaseResponse<UsersEntity>> homeInfluencers(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/celebrity/{celebrityId}")
    Observable<BaseResponse<CelebrityEntity>> homeCelebrityList(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/users/TopFan")
    Observable<BaseResponse<BaseDataPagingResponseModel<TopFanModel>>> getTopFan(@Header("Authorization") String authen, @Body GetTopFanModel request);

    @GET("api/users/celebrity/{celebrityId}")
    Observable<BaseResponse<CelebrityProfileEntity>> celebrityProfile(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId);

    @GET("api/challenges/{challengeId}")
    Observable<BaseResponse<CelebrityListEntity>> getChallenge(@Header("Authorization") String connection, @Path("challengeId") int celebrityId);

    @GET("api/challenges/{challengeId}/entries")
    Observable<BaseResponse<ChallengeEntriesEntityResponse>> getChallengeEntries(@Header("Authorization") String connection, @Path("challengeId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/entry/{entryId}")
    Observable<BaseResponse<ContestantEntriesEntity>> viewContestantChallengesEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/challenges/entry/{entryId}/star")
    Observable<BaseResponse<SendGiftEntity>> sendStart(@Header("Authorization") String connection, @Path("entryId") int entryId, @Body SendStartRequestModel request);

    @GET("api/users/{userId}/fans")
    Observable<BaseResponse<TopFansEntity>> getTopFans(@Header("Authorization") String connection, @Path("userId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges")
    Observable<BaseResponse<ChallengeListEntriesEntityResponse>> getChallengeListEntries(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/users/RegisterWithGoogle")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithGoogle(@Body MultipartBody request);

    @POST("api/users/RegisterWithFacebook")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithFacebook(@Body MultipartBody request);

    @POST("api/users/RegisterWithInstagram")
    Observable<BaseResponse<RegisterWithFacebookResponseModel>> registerWithInstagram(@Body MultipartBody request);

    @POST("api/challenges")
    Observable<BaseResponse<PostDataModel>> postCreatePost(@Header("Authorization") String authen, @Body MultipartBody request);

    @POST("api/users/LogOut")
    Observable<BaseResponse<Boolean>> logoutApp(@Header("Authorization") String authen, @Body LogoutRequestModel request);

    @GET("api/notifications/category/{category}")
    Observable<BaseResponse<BaseDataPagingResponseModel<NotificationItemModel>>> getNotificationList(@Header("Authorization") String authen, @Path("category") int category, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/star/history")
    Observable<BaseResponse<StarEntity>> getStarsList(@Header("Authorization") String authen, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/loyalty/history")
    Observable<BaseResponse<LoyaltyEntity>> getLoyaltyList(@Header("Authorization") String authen, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/merchants")
    Observable<BaseResponse<UsersEntity>> getMerchants(@Header("Authorization") String connection, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/challenges/celebrity/{celebrityId}/gridview")
    Observable<BaseResponse<CelebrityGridEntity>> celebrityGrid(@Header("Authorization") String connection, @Path("celebrityId") int celebrityId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/payments/gift")
    Observable<BaseResponse<GiftStoreEntity>> getGiftStore(@Header("Authorization") String authen);

    @POST("api/challenges/{challengeId}/submission")
    Observable<BaseResponse<PostDataModel>> submissionChallenge(@Header("Authorization") String authen, @Path("challengeId") int challengeId, @Body MultipartBody request);

    @POST("api/payments/gift")
    Observable<BaseResponse<SendGiftEntity>> sendGift(@Header("Authorization") String authen, @Body SendGiftRequest request);

    @POST("api/challenges")
    Observable<BaseResponse<PostDataModel>> createChallenge(@Header("Authorization") String authen, @Body MultipartBody request);

    @POST("api/challenges/{challengeId}/Edit")
    Observable<BaseResponse<PostDataModel>> editChallenge(@Header("Authorization") String authen, @Path("challengeId") int challengeId, @Body MultipartBody request);

    @GET("api/users/username/{username}/isexist")
    Observable<BaseResponse<Boolean>> verifyUsername(@Path("username") String username);

    @POST("api/users/EditProfile")
    Observable<BaseResponse<SettingResponse>> updateProfile(@Header("Authorization") String authen, @Body MultipartBody request);

    @POST("api/users/follow")
    Observable<BaseResponse<FollowUserEntity>> followUser(@Header("Authorization") String connection, @Body FollowUserRequestModel request);

    @POST("api/challenges/{challengeId}/like")
    Observable<BaseResponse<Integer>> likePost(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/entry/{entryId}/like")
    Observable<BaseResponse<Integer>> likeEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/challenges/{challengeId}/unlike")
    Observable<BaseResponse<Integer>> unlike(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/entry/{entryId}/unlike")
    Observable<BaseResponse<Integer>> unlikeEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/users/unfollow")
    Observable<BaseResponse<FollowUserEntity>> unFollowUser(@Header("Authorization") String connection, @Body FollowUserRequestModel request);

    @GET("api/users/{userId}/following")
    Observable<BaseResponse<BaseDataPagingResponseModel<FollowEntity>>> getFollowingList(@Header("Authorization") String authen, @Path("userId") int userId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/{userId}/followers")
    Observable<BaseResponse<BaseDataPagingResponseModel<FollowEntity>>> getFollowersList(@Header("Authorization") String authen, @Path("userId") int userId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/comments/challenge/{challengeId}")
    Observable<BaseResponse<BaseDataPagingResponseModel<CommentsListEntity>>> getChallengeCommentsList(@Header("Authorization") String authen, @Path("challengeId") int challengeId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/comments/submission/{submissionId}")
    Observable<BaseResponse<BaseDataPagingResponseModel<CommentsListEntity>>> getEntriesCommentsList(@Header("Authorization") String authen, @Path("submissionId") int challengeId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/comments/challenge")
    Observable<BaseResponse<AddCommentEntity>> addChallengeComment(@Header("Authorization") String connection, @Body AddCommentRequestModel request);

    @POST("api/comments/submission")
    Observable<BaseResponse<AddCommentEntity>> addEntriesComment(@Header("Authorization") String connection, @Body AddCommentRequestModel request);

    @GET("api/challenges/{challengeId}/CanSubmit")
    Observable<BaseResponse<Boolean>> canSubmitChallenge(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @GET("api/users/referral/{referralCode}/isexist")
    Observable<BaseResponse<Boolean>> isReferralCodeExist(@Path("referralCode") String referralCode);

    @GET("api/users/email/{email}/isexist")
    Observable<BaseResponse<Boolean>> isEmailExist(@Path("email") String email);

    @GET("api/payments/product")
    Observable<BaseResponse<GemResponse>> getRefillList(@Header("Authorization") String connection);

    @GET("api/users/{userId}/followers")
    Observable<BaseResponse<BaseDataPagingResponseModel<FollowItem>>> getFollowersTagList(@Header("Authorization") String authen, @Path("userId") int userId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/users/username/{username}")
    Observable<BaseResponse<CelebrityProfileEntity>> getUserProfile(@Header("Authorization") String connection, @Path("username") String username);

    @POST("api/comments/{commentId}/challenge/{challengeId}")
    Observable<BaseResponse<DeleteCommentEntity>> deleteChallengeComment(@Header("Authorization") String connection, @Path("commentId") int commentId, @Path("challengeId") int challengeId);

    @POST("api/comments/{commentId}/submission/{submissionId}")
    Observable<BaseResponse<DeleteCommentEntity>> deleteSubmissionComment(@Header("Authorization") String connection, @Path("commentId") int commentId, @Path("submissionId") int submissionId);

    @DELETE("api/challenges/{challengeId}")
    Observable<BaseResponse<Boolean>> deleteChallenge(@Header("Authorization") String authen, @Path("challengeId") int challengeId);

    @POST("api/users/{userId}/block")
    Observable<BaseResponse<Boolean>> blockUser(@Header("Authorization") String connection, @Path("userId") int userId);

    @POST("api/challenges/{challengeId}/report")
    Observable<BaseResponse<Boolean>> reportPost(@Header("Authorization") String connection, @Path("challengeId") int challengeId, @Body ReportRequestModel reason);

    @POST("api/challenges/{challengeId}/unreport")
    Observable<BaseResponse<Boolean>> unReportPost(@Header("Authorization") String connection, @Path("challengeId") int challengeId);

    @POST("api/challenges/{challengeId}/Edit")
    Observable<BaseResponse<PostDataModel>> editPost(@Header("Authorization") String authen, @Path("challengeId") int challengeId, @Body MultipartBody request);

    @GET("api/users/blocks")
    Observable<BaseResponse<BaseDataPagingResponseModel<BlockedUserModel>>> getBlockedUsers(@Header("Authorization") String authen, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @POST("api/users/{userId}/unblock")
    Observable<BaseResponse<Boolean>> unblockUser(@Header("Authorization") String authen, @Path("userId") int userId);

    @POST("api/challenges/entry/{entryId}/report")
    Observable<BaseResponse<Boolean>> reportEntries(@Header("Authorization") String connection, @Path("entryId") int entryId, @Body ReportRequestModel reason);

    @POST("api/challenges/entry/{entryId}/unreport")
    Observable<BaseResponse<Boolean>> unReportEntries(@Header("Authorization") String connection, @Path("entryId") int entryId);

    @POST("api/challenges/entry/{entryId}/edit")
    Observable<BaseResponse<PostDataModel>> editSubmission(@Header("Authorization") String authen, @Path("entryId") int entryId, @Body MultipartBody request);

    @DELETE("api/challenges/entry/{entryId}")
    Observable<BaseResponse<Boolean>> deleteSubmission(@Header("Authorization") String authen, @Path("entryId") int challengeId);

    @POST("api/payments/InAppPurchase")
    Observable<BaseResponse<VerifyIAPResponeModel>> verifyIAPPurchased(@Header("Authorization") String authen, @Body VerifyIAPRequestModel request);

    @GET("api/payments/InAppPurchase/{transactionId}/isfinished")
    Observable<BaseResponse<Boolean>> iAPPurchasedIsfinished(@Header("Authorization") String authen, @Path("transactionId") String transactionId);

    @GET("api/settings/app/configs")
    Observable<BaseResponse<VersionResponseModel>> checkVersion(@Query("model.deviceType") int deviceType, @Query("model.version") String version);

    @GET("api/users/leftmenu")
    Observable<BaseResponse<CreditsModel>> getUserCredits(@Header("Authorization") String authen);

    @POST("api/redemption/code/{promotionCode}")
    Observable<BaseResponse<RedemptionResponse>> addRedemptionCode(@Header("Authorization") String authen, @Path("promotionCode") String promotionCode);

    @GET("api/prize/boxes")
    Observable<BaseResponse<BoxListResponse>> getRewardList(@Header("Authorization") String authen, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/prize/boxes/{ownerId}/items")
    Observable<BaseResponse<BaseDataPagingResponseModel<PrizeEntity>>> getPrizeList(@Header("Authorization") String authen, @Path("ownerId") int ownerId, @Query("model.nextId") int nextId, @Query("model.limit") int limit);

    @GET("api/prize/boxes/owner/{ownerId}")
    Observable<BaseResponse<List<BoxesTypeEntity>>> getBoxesList(@Header("Authorization") String authen, @Path("ownerId") int ownerId);

    @POST("api/prize/boxes/{boxId}/draw")
    Observable<BaseResponse<UsePointsResponsModel>> usePoints(@Header("Authorization") String authen, @Path("boxId") int boxId);

    @POST("api/prize/bag/{bagId}/redeem")
    Observable<BaseResponse<Boolean>> bagRedeem(@Header("Authorization") String authen, @Path("bagId") int bagId, @Body BagRedeemRequestModel request);

    @GET("api/prize/bag/unredeemed")
    Observable<BaseResponse<List<UsePointsResponsModel>>> checkUnredeemed(@Header("Authorization") String authen);

    @GET("api/prize/boxes/owner/{ownerId}/packages")
    Observable<BaseResponse<CelebrityBoxesModel>> getBoxes(@Header("Authorization") String authen, @Path("ownerId") int ownerId);

}

package com.gcox.fansmeet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfigModel {

	@SerializedName("HoursCacheStatisResources")
	@Expose
	public int hoursCacheStatisResources;

	@SerializedName("EnableCashOutIOS")
	@Expose
	public boolean enableCashOutIOS;

	@SerializedName("EnableNewCashOutAndroid")
	@Expose
	public boolean enableCashOutAndroid;

	@SerializedName("LuckyWheelStatus")
	@Expose
	public boolean luckyWheelStatus;

	@SerializedName("ProfileImageUrl")
	@Expose
	public String profileImageUrl;

	@SerializedName("EnableStreamTitle")
	@Expose
	public boolean enableStreamTitle;

	@SerializedName("TextRegisterInviteFriend") @Expose
	public String rewardMsgFriendSuggestion;

	@SerializedName("TextInviteFriendHeader") @Expose
	public String rewardMsgInvitationHeader;

	@SerializedName("TextInviteFriendFooter") @Expose
	public String rewardMsgInvitationFooter;

	@SerializedName("EnableNewTab") @Expose
	public boolean enableNewTab;

	@SerializedName("EnableNearbyTab") @Expose
	public boolean enableNearbyTab;

	@SerializedName("EnableNewVideoCall") @Expose
	public boolean enableVideoCall;

	@SerializedName("InviteFriendViaContactMsg") @Expose
	public String inviteFriendMessageContent;

	/**
	 * nextTreatTimeLeft is in seconds
	 */
	@SerializedName("RemainingDailyBonusTime") @Expose
	public int nextTreatTimeLeft;

	@SerializedName("IOSVersion") @Expose
	public String newestBeliveIosVersion;
	@SerializedName("AndroidVersion") @Expose
	public String newestBeliveAndroidVersion;
}
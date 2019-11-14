package com.gcox.fansmeet.features.rewards.dialog

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.SystemClock
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.Toast
import com.gcox.fansmeet.AppsterApplication

import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation
import com.gcox.fansmeet.core.dialog.NoTitleDialogFragment
import com.gcox.fansmeet.core.itemdecorator.VerticalSpaceItemDecoration
import com.gcox.fansmeet.customview.CustomTypefaceSpan
import com.gcox.fansmeet.customview.hashtag.SpannableUtil
import com.gcox.fansmeet.features.prizelist.PrizeListActivity
import com.gcox.fansmeet.features.prizelist.models.PrizeCost
import com.gcox.fansmeet.features.prizelist.models.PrizeType
import com.gcox.fansmeet.features.profile.userprofile.FragmentMe
import com.gcox.fansmeet.features.rewards.adapter.RewardAdapter
import com.gcox.fansmeet.features.rewards.models.*
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DialogManager
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.webservice.AppsterWebServices
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_reward_popup.*
import timber.log.Timber

class RewardDialog : NoTitleDialogFragment(),
    OnDisplayableItemClicked, PrizeRevealPrizeDialog.DaylyTreatPrizeListner {


    val adapter by lazy { RewardAdapter(items = ArrayList<BoxesModel>(), listener = this) }
    private var rewardItem: RewardItem? = null
    private var compositeDisposable: CompositeDisposable? = null
    var listener: PlayStatusListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvPrizeOptions.adapter = adapter

        if (arguments != null) {
            rewardItem = arguments!!.getParcelable(REWARD_MODEL)
            rcvPrizeOptions.addItemDecoration(VerticalSpaceItemDecoration(Utils.dpToPx(10f)))
            setRewardMessage()
            val celebrityBoxesModel: CelebrityBoxesModel? = arguments!!.getParcelable(CELEBRITY_BOXES_MODEL)
            if (celebrityBoxesModel != null) {
                if (celebrityBoxesModel.boxes != null) {
                    adapter.updateItems(celebrityBoxesModel.boxes)
                }
                tvBalanceValue.text = SpannableUtil.replaceSquareGemIcon(
                    context,
                    String.format(
                        "%s pts + %s :gem:",
                        celebrityBoxesModel.loyaltyBalance,
                        celebrityBoxesModel.gemBalance
                    )
                )
            }
        }
        imClose.setOnClickListener { dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (compositeDisposable != null && !compositeDisposable!!.isDisposed) compositeDisposable!!.dispose()
    }

    override fun onDisplayableItemClicked(v: View, item: DisplayableItem) {
        if (item is BoxesModel) {
            DialogbeLiveConfirmation.Builder().confirmText(getString(R.string.button_play))
                .cancelText(getString(R.string.btn_text_cancel))
                .title(getString(R.string.use_points))
                .message(
                    SpannableUtil.replaceGemIcon(
                        context,
                        String.format("%s %s for %s", rewardItem?.user?.displayName, item.name, item.getPrizeCost())
                    )
                )
                .onConfirmClicked {
                    usePoint(item)
                }
                .build().show(context)
        }
    }

    private fun usePoint(item: BoxesModel) {
        if (!CheckNetwork.isNetworkAvailable(context)) {
            DialogInfoUtility().showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                context
            )
            return
        }
        DialogManager.getInstance().showDialog(context, context?.getString(R.string.connecting_msg))
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().usePoints(AppsterUtility.getAuth(), item.id!!)
                .subscribe({ usePointsResponse ->
                    DialogManager.getInstance().dismisDialog()
                    if (usePointsResponse.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK && usePointsResponse.data != null) {
                        showRedeemDialog(usePointsResponse.data)

                    } else {
                        handleErrorCode(usePointsResponse.code, usePointsResponse.message)
                    }

                }, { error ->
                    DialogManager.getInstance().dismisDialog()
                    handleErrorCode(Constants.RETROFIT_ERROR, error.message!!)
                })
        )

    }

    private fun showRedeemDialog(usePointsResponse: UsePointsResponsModel) {
        val redeemDialog = PrizeRevealPrizeDialog.newInstance(
            PrizeCollectModel(
                usePointsResponse.id!!,
                usePointsResponse.itemName!!,
                usePointsResponse.title,
                usePointsResponse.description,
                usePointsResponse.image!!
            )
        )
        redeemDialog.show(childFragmentManager, PrizeRevealPrizeDialog::class.java.name)
        redeemDialog.setDaylyTreatPrizeListner(this)
    }

    override fun sendEmailSuccess(boxId: Int) {
        if (context != null)
            DialogbeLiveConfirmation.Builder().title(getString(R.string.redeem_success_title))
                .message(getString(R.string.redeem_success_message))
                .singleAction(true)
                .confirmText(getString(R.string.okay))
                .onConfirmClicked { getBoxes(rewardItem!!.user.ownerId!!) }
                .build().show(context)
    }

    private fun setRewardMessage() {
        val clickablePP = object : ClickableSpan() {

            override fun onClick(widget: View) {
                if (listener != null && rewardItem != null) listener!!.onOpenPrizeList(rewardItem!!)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = Color.parseColor("#6b6c6e")
            }
        }
        val message = getString(R.string.reward_message)
        val prizeListTitle = getString(R.string.prize_list_title)
        val spannablecontent = SpannableString(message)

        var start = message.indexOf(prizeListTitle)
        val end = message.indexOf(prizeListTitle) + prizeListTitle.length

        spannablecontent.setSpan(clickablePP, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannablecontent.setSpan(RelativeSizeSpan(1.0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannablecontent.setSpan(
            ForegroundColorSpan(Color.parseColor("#278BED")),
            start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val builder = StringBuilder()
        builder.append(spannablecontent)
        tvDescribe.movementMethod = LinkMovementMethod.getInstance()
        tvDescribe.append(spannablecontent)
    }

    private fun updateBalance(item: PrizeType?) {
        DialogManager.getInstance().showDialog(context, context?.getString(R.string.connecting_msg))
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().getUserCredits("Bearer " + AppsterApplication.mAppPreferences.userToken)
                .subscribe({ creditsResponseModel ->
                    if (creditsResponseModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        AppsterApplication.mAppPreferences.userModel.stars = creditsResponseModel.data.stars
                        AppsterApplication.mAppPreferences.userModel.gems = creditsResponseModel.data.gems
                        AppsterApplication.mAppPreferences.userModel.loyalty = creditsResponseModel.data.loyalty

                        tvBalanceValue.text = SpannableUtil.replaceSquareGemIcon(
                            context,
                            String.format(
                                "%s pts + %s :gem:",
                                creditsResponseModel.data.loyalty,
                                creditsResponseModel.data.gems
                            )
                        )
                    }
                    DialogManager.getInstance().dismisDialog()
                }, { error ->
                    Timber.e(error)
                    DialogManager.getInstance().dismisDialog()
                })
        )
    }

    private fun getBoxes(ownerId: Int) {
        DialogManager.getInstance().showDialog(context, context?.getString(R.string.connecting_msg))
        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().getBoxes("Bearer " + AppsterApplication.mAppPreferences.userToken, ownerId)
                .subscribe({ boxesModel ->
                    if (boxesModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {

                        if (boxesModel.data.boxes != null) {
                            adapter.updateItems(boxesModel.data.boxes)
                        }
                        tvBalanceValue.text = SpannableUtil.replaceSquareGemIcon(
                            context,
                            String.format(
                                "%s pts + %s :gem:",
                                boxesModel.data.loyaltyBalance,
                                boxesModel.data.gemBalance
                            )
                        )
                    }
                    DialogManager.getInstance().dismisDialog()
                }, { error ->
                    Timber.e(error)
                    DialogManager.getInstance().dismisDialog()
                })
        )
    }


    private fun handleErrorCode(code: Int, message: String) {
        if (context != null) {
            DialogbeLiveConfirmation.Builder()
                .title(context!!.getString(R.string.app_name))
                .singleAction(true)
                .message(message)
                .build().show(context)
        }

    }

    fun setPlayStatusListener(listener: PlayStatusListener) {
        this.listener = listener
    }

    override fun getRootLayoutResource(): Int {
        return R.layout.dialog_reward_popup
    }

    override fun isDimDialog(): Boolean {
        return false
    }

    companion object {
        private const val REWARD_MODEL = "reward_model"
        private const val CELEBRITY_BOXES_MODEL = "celebrity_boxes_model"
        fun newInstance(rewardItem: RewardItem, celebrityBoxesModel: CelebrityBoxesModel): RewardDialog {
            val args = Bundle()
            val fragment = RewardDialog()
            args.putParcelable(REWARD_MODEL, rewardItem)
            args.putParcelable(CELEBRITY_BOXES_MODEL, celebrityBoxesModel)
            fragment.arguments = args
            return fragment
        }
    }

    interface PlayStatusListener {
        fun onOpenPrizeList(rewardItem: RewardItem)
    }
}

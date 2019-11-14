package com.gcox.fansmeet.features.rewards

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gcox.fansmeet.BuildConfig
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.activity.HandleErrorActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.OnDisplayableItemClicked
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation
import com.gcox.fansmeet.features.prizelist.PrizeListActivity
import com.gcox.fansmeet.features.prizelist.models.PrizeType
import com.gcox.fansmeet.features.rewards.adapter.RewardAdapter
import com.gcox.fansmeet.features.rewards.dialog.PrizeRevealPrizeDialog
import com.gcox.fansmeet.features.rewards.dialog.RewardDialog
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.UsePointsResponsModel
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DialogManager
import com.gcox.fansmeet.util.UiUtils
import com.gcox.fansmeet.webview.ActivityViewWeb
import kotlinx.android.synthetic.main.activity_reward.*
import kotlinx.android.synthetic.main.content_reward.*
import kotlinx.android.synthetic.main.content_reward.swiperefresh
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class RewardActivity : HandleErrorActivity(), OnDisplayableItemClicked, OnLoadMoreListenerRecyclerView,
    RewardDialog.PlayStatusListener, PrizeRevealPrizeDialog.DaylyTreatPrizeListner {

    val adapter by lazy { RewardAdapter(items = ArrayList<RewardItem>(), listener = this) }
    private val rewardViewModel: RewardViewModel by viewModel()

    private var nextId = Constants.WEB_SERVICE_START_PAGE
    private var isEnded = false
    private var clickPlayRewardItem: RewardItem? = null

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, RewardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener { refreshData() }
        rcvRewards.setOnLoadMoreListener(this)

        rcvRewards.adapter = adapter
        observeData()

        DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
        rewardViewModel.checkUnredeemed()
    }

    override fun onLoadMore() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            if (!isEnded) rewardViewModel.getRewardList(nextId, Constants.PAGE_LIMITED)
        }
    }

    private fun observeData() {

        rewardViewModel.getError().observe(this, Observer {
            DialogManager.getInstance().dismisDialog()
            rcvRewards.setLoading(false)
            it?.let {
                handleError(it.message, it.code)
            }
        })

        rewardViewModel.getRewardList.observe(this, Observer {
            adapter.updateItems(it?.listBox)
            DialogManager.getInstance().dismisDialog()
            nextId = it?.nextId!!
            isEnded = it.isEnded
            rcvRewards.setLoading(false)
            showNothingText()

        })

        rewardViewModel.checkUnredeemed.observe(this, Observer {
            DialogManager.getInstance().dismisDialog()
            if (!it.isNullOrEmpty()) {
                val redeemDialog = PrizeRevealPrizeDialog.newInstance(it[0])
                redeemDialog.show(supportFragmentManager, PrizeRevealPrizeDialog::class.java.name)
                redeemDialog.setDaylyTreatPrizeListner(this)
            } else {
                DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
                rewardViewModel.getRewardList(nextId, Constants.PAGE_LIMITED)
            }
        })

        rewardViewModel.getPackages.observe(this, Observer {
            DialogManager.getInstance().dismisDialog()
            it?.let {
                if (clickPlayRewardItem != null) {
                    val rewardDialog = RewardDialog.newInstance(clickPlayRewardItem!!, it)
                    rewardDialog.show(supportFragmentManager, RewardDialog::class.java.name)
                    rewardDialog.setPlayStatusListener(this)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reward, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.mnHowToPlay) {
                startActivity(
                    ActivityViewWeb.createIntent(
                        this,
                        BuildConfig.API_ENDPOINT + Constants.URL_REWARDS_HOW_TO_PLAY_END_LINK,
                        true
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDisplayableItemClicked(v: View, item: DisplayableItem) {
        Timber.e("onDisplayableItemClicked %s", v.id)
        AppsterUtility.temporaryLockView(v)
        when (v.id) {
            R.id.btnPlay -> {
                if (item is RewardItem) {
                    clickPlayRewardItem = item
                    item.user.ownerId?.let {
                        DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
                        rewardViewModel.getPackages(item.user.ownerId)
                    }
                }
            }
            R.id.tvPrizeList -> {
                if (item is RewardItem) {
                    startActivity(
                        PrizeListActivity.createIntent(
                            this, item.user.ownerId!!, item.user.displayName
                        )
                    )
                }
            }
            else -> Unit
        }
    }

    override fun onOpenPrizeList(rewardItem: RewardItem) {
        startActivity(
            PrizeListActivity.createIntent(
                this, rewardItem.user.ownerId!!, rewardItem.user.displayName
            )
        )
    }

    override fun sendEmailSuccess(boxId: Int) {
        DialogbeLiveConfirmation.Builder().title(getString(R.string.redeem_success_title))
            .message(getString(R.string.redeem_success_message))
            .singleAction(true)
            .confirmText(getString(R.string.okay))
            .onConfirmClicked {
                DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
                rewardViewModel.getRewardList(nextId, Constants.PAGE_LIMITED)
            }
            .build().show(this)
    }

    private fun showNothingText() {
        if (adapter.itemCount > 0) {
            tvNothing.visibility = View.GONE
            rcvRewards.visibility = View.VISIBLE
        } else {
            tvNothing.visibility = View.VISIBLE
            rcvRewards.visibility = View.GONE
        }
    }

    private fun refreshData() {
        if (CheckNetwork.isNetworkAvailable(applicationContext)) {
            nextId = Constants.WEB_SERVICE_START_PAGE
            isEnded = false
            swiperefresh?.isRefreshing = false
            DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg))
            rewardViewModel.getRewardList(nextId, Constants.PAGE_LIMITED)
        } else {
            val utility = DialogInfoUtility()
            utility.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this
            )
        }
    }

}

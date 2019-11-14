package com.appster.features.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate

import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView
import com.gcox.fansmeet.core.fragment.BaseFragment
import com.gcox.fansmeet.features.notification.NotificationItemModel
import com.gcox.fansmeet.features.notification.NotifyContract
import com.gcox.fansmeet.features.notification.NotifyPresenter
import com.gcox.fansmeet.features.notification.NotifyRecyclerviewAdapter
import com.gcox.fansmeet.features.profile.userprofile.FragmentMe
import com.gcox.fansmeet.features.profile.userprofile.ListFragment
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.PixelUtil
import com.gcox.fansmeet.util.UiUtils

import java.util.ArrayList
import kotlinx.android.synthetic.main.fragment_notify_you.*

/**
 * Created by ngoc on 10/8/2015.
 */
class NotifyFragment : BaseFragment(), NotifyContract.View {

    private var arrListNotify = ArrayList<NotificationItemModel>()
    private var notifyAdapter: NotifyRecyclerviewAdapter? = null
    private var presenter: NotifyContract.UserActions? = null

    private var nextIndex: Int = Constants.WEB_SERVICE_START_PAGE
    private var isTheEnd = false
    private var notifyType = NotifyType.CelebConnect.value

    enum class NotifyType(val value: Int) {
        CelebConnect(1),
        FanConnect(2),
        GachaPrizes(3)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            notifyType = arguments!!.getInt(BUNDLE_NOTIFY_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_notify_you)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = NotifyPresenter(this)
        setEventListView()
        if (notifyType == NotifyType.CelebConnect.value) {
            presenter?.getNotificationList(notifyType, nextIndex, true)
        }
    }

    private fun setEventListView() {

        lvNotifyYou.layoutManager = LinearLayoutManager(context)
        UiUtils.setColorSwipeRefreshLayout(swiperefresh)
        swiperefresh.setOnRefreshListener {
            nextIndex = Constants.WEB_SERVICE_START_PAGE
            presenter?.getNotificationList(notifyType, nextIndex, true)
        }
        lvNotifyYou.addItemDecoration(
            UiUtils.ListSpacingItemDecoration(
                PixelUtil.dpToPx(context, 5),
                false
            )
        )

        notifyAdapter =
            NotifyRecyclerviewAdapter(activity!!, lvNotifyYou, arrListNotify, Constants.NOTIFYCATION_TYPE_YOU)
        lvNotifyYou.adapter = notifyAdapter
        notifyAdapter?.setOnLoadMoreListener {
            if (CheckNetwork.isNetworkAvailable(context)) {
                if (!isTheEnd) presenter?.getNotificationList(notifyType, nextIndex, false)
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && !areLecturesLoaded &&
            (notifyType == NotifyType.FanConnect.value || notifyType == NotifyType.GachaPrizes.value)
        ) {
            presenter?.getNotificationList(notifyType, nextIndex, true)
            areLecturesLoaded = true
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onHandleUiAfterApiReturn() {
        dismissDialog()
        swiperefresh?.isRefreshing = false
//        notifyAdapter?.removeProgressItem()
//        notifyAdapter?.setLoaded()
    }

    override fun getViewContext(): Context? {
        return context
    }

    override fun loadError(errorMessage: String?, code: Int) {
        if (isFragmentUIActive) onErrorWebServiceCall(errorMessage, code)
    }

    override fun showProgress() {
        showDialog()
    }

    override fun hideProgress() {
        dismissDialog()
    }

    fun fakeData(): List<NotificationItemModel> {
        val items = arrayListOf<NotificationItemModel>()
        for (i in 1..30) {
            items.add(NotificationItemModel())
        }

        return items
    }

    override fun setDataForListView(data: List<NotificationItemModel>, isEnded: Boolean, nextIndex: Int) {

        if (this.nextIndex == Constants.WEB_SERVICE_START_PAGE) arrListNotify.clear()
        if (data.isNotEmpty()) arrListNotify.addAll(data)
        onHandleUiAfterApiReturn()
        notifyAdapter!!.notifyDataSetChanged()
        if (arrListNotify.isNotEmpty()) {
            no_data_view?.visibility = View.GONE
            lvNotifyYou.visibility = View.VISIBLE
        } else {
            no_data_view?.visibility = View.VISIBLE
            lvNotifyYou.visibility = View.GONE
        }
        this.nextIndex = nextIndex
        isTheEnd = isEnded
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        private const val BUNDLE_NOTIFY_TYPE = "BUNDLE_NOTIFY_TYPE"

        @JvmStatic
        fun newInstance(notifyType: NotifyType): NotifyFragment {
            val f = NotifyFragment()
            val args = Bundle()
            args.putInt(BUNDLE_NOTIFY_TYPE, notifyType.value)
            f.arguments = args
            return f
        }
    }
}

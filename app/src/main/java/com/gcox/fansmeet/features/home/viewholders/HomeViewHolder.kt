package com.gcox.fansmeet.features.home.viewholders

import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.decodeEmoji
import com.appster.extensions.inflate
import com.appster.extensions.loadImg
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.home.CelebritiesMode
import com.gcox.fansmeet.util.StringUtil
import kotlinx.android.synthetic.main.live_show_item_multi.view.*


/**
 * Created by ngoc on 5/16/18.
 */

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): HomeViewHolder {
            return HomeViewHolder(parent.inflate(layout))
        }
    }

    fun bindTo(item: CelebritiesMode?, listener: OnClickListener?) {
        with(itemView) {
            item?.let {
                ivShowImg.loadImg(item.bannerImage)
                item.isFollow?.let {
                    if (item.isFollow!!) {
                        btnFollow.setBackgroundResource(R.drawable.home_following_btn_selector)
                        btnFollow.setTextColor(ContextCompat.getColor(context, R.color.color_2587E7))
                        btnFollow.text = context.getString(R.string.profile_following_title)
                    } else {
                        btnFollow.setBackgroundResource(R.drawable.home_follow_btn_selector)
                        btnFollow.setTextColor(ContextCompat.getColor(context, R.color.white))
                        btnFollow.text = context.getString(R.string.follow)
                    }
                }

                if (item.userId == AppsterApplication.mAppPreferences.userModel.userId) {
                    itemView.btnFollow.visibility = View.GONE
                } else {
                    itemView.btnFollow.visibility = View.VISIBLE
                    itemView.btnFollow.setOnClickListener { listener?.onFollowClicked(item, adapterPosition) }
                }

                if (item.isClickable != null && item.isClickable!! == Constants.HOME_IMAGE_CAN_CLICK_ABLE) {
                    ivShowImg.setOnClickListener {
                        listener?.onUserImageClicked(
                            item
                        )
                    }
                }
//                if (item.onlyShowBannerImage != null && item.onlyShowBannerImage == 1) {
                userImage.visibility = View.GONE
                displayName.visibility = View.GONE
                about.visibility = View.GONE
                description.visibility = View.GONE
                rlUserInfo.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
//                } else {
//                    userImage.visibility = View.VISIBLE
//                    displayName.visibility = View.VISIBLE
//                    about.visibility = View.VISIBLE
//                    description.visibility = View.VISIBLE
//                    userImage.loadImg(item.userImage)
//                    displayName.text = item.displayName?.decodeEmoji()
//                    item.about?.let {
//                        about.text = StringUtil.decodeString(it)
//                    }
//                    description.text = StringUtil.decodeString(item.description)
//                    rlUserInfo.background = ContextCompat.getDrawable(context, R.drawable.bg_black75_gradient_login)
//                }
            }
        }
    }

    interface OnClickListener {
        fun onFollowClicked(item: CelebritiesMode, position: Int)

        fun onUserImageClicked(item: CelebritiesMode)
    }
}
